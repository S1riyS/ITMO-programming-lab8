package s1riys.lab8.server.network;

import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.Logger;
import s1riys.lab8.common.network.UDPShared;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.NoSuchCommandResponse;
import s1riys.lab8.common.network.responses.Response;
import s1riys.lab8.server.Main;
import s1riys.lab8.server.handlers.CommandHandler;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static s1riys.lab8.common.constants.Network.*;

public class UDPServer extends UDPShared {
    private final Map<SocketAddress, byte[]> buffers = new HashMap<>();
    private final DatagramSocket datagramSocket;
    private final CommandHandler commandHandler;
    private boolean running = true;
    private final Logger logger = Main.logger;
    private static final int HANDLE_POOL_SIZE = 4;
    private static final int SEND_POOL_SIZE = 4;
    private final ExecutorService handleService;
    private final ExecutorService sendService;

    public UDPServer(InetAddress address, int port, CommandHandler commandHandler) throws SocketException {
        super(address, port);
        this.datagramSocket = new DatagramSocket(getAddr());
        this.datagramSocket.setReuseAddress(true);
        this.commandHandler = commandHandler;

        this.handleService = Executors.newFixedThreadPool(HANDLE_POOL_SIZE);
        this.sendService = Executors.newFixedThreadPool(SEND_POOL_SIZE);
    }

    public void listenToClients() throws IOException {
        var data = new byte[PACKET_SIZE];

        var dp = new DatagramPacket(data, PACKET_SIZE);
        datagramSocket.receive(dp);
        SocketAddress addr = dp.getSocketAddress();
        logger.info("Получено \"{}\" от {}", new String(data), dp.getAddress());

        byte[] sanitizedData = Arrays.copyOf(data, data.length - 1);

        if (buffers.get(addr) == null) {
            buffers.put(addr, sanitizedData);
        } else {
            byte[] currentClientBuffer = buffers.get(addr);
            byte[] updatedClientBuffer = Bytes.concat(currentClientBuffer, sanitizedData);
            buffers.put(addr, updatedClientBuffer);
        }

        if (data[data.length - 1] == STOP_BYTE) {
            logger.info("Получение данных от {} завершено", dp.getAddress());
            handleService.submit(() -> handleClientRequest(addr));
        }
    }

    private void handleClientRequest(SocketAddress address) {
        // Retrieving client's bytes
        Byte[] dataFromClient = ArrayUtils.toObject(buffers.get(address));
        buffers.remove(address);

        // Deserializing request
        Request request;
        try {
            request = SerializationUtils.deserialize(ArrayUtils.toPrimitive(dataFromClient));
            logger.info("Обработка {} от {}", request, address);

        } catch (Exception e) {
            logger.error("Невозможно десериализовать объект запроса", e);
            disconnectFromClient();
            return;
        }

        // Forming response
        Response response = null;
        try {
            response = commandHandler.handle(request);
        } catch (Exception e) {
            logger.error("Ошибка выполнения команды: {}", e.toString(), e);
        }
        if (response == null) response = new NoSuchCommandResponse(request.getName());

        var data = SerializationUtils.serialize(response);
        logger.info("Ответ: {}", response);

        // Sending data to client
        sendService.submit(() -> {
            try {
                sendData(data, address);
                logger.info("Отправлен ответ клиенту по адресу {}", address);
            } catch (Exception e) {
                logger.error("Ошибка ввода-вывода: {}", e.toString(), e);
            }
        });

        // Disconnecting from client
        disconnectFromClient();
        logger.info("Отключение от клиента {}", address);
    }

    public void sendData(byte[] data, SocketAddress addr) throws IOException {
        byte[][] dataChuncks = this.generateDataChuncks(data);

        logger.info("Отправление чанков... ({} шт.)", dataChuncks.length);
        for (int i = 0; i < dataChuncks.length; i++) {
            var chunk = dataChuncks[i];
            if (i == dataChuncks.length - 1) {
                var lastChunk = Bytes.concat(chunk, new byte[]{STOP_BYTE});
                var dp = new DatagramPacket(lastChunk, PACKET_SIZE, addr);
                datagramSocket.send(dp);
                logger.info("Последний чанк размером " + lastChunk.length + " отправлен на клиент.");
            } else {
                var dp = new DatagramPacket(ByteBuffer.allocate(PACKET_SIZE).put(chunk).array(), PACKET_SIZE, addr);
                datagramSocket.send(dp);
                logger.info("Чанк размером " + chunk.length + " отправлен на клиент.");
            }
        }

        logger.info("Отправка данных завершена");
    }

    public void connectToClient(SocketAddress addr) throws SocketException {
        datagramSocket.connect(addr);
    }

    public void disconnectFromClient() {
        datagramSocket.disconnect();
    }

    public void close() {
        datagramSocket.close();
    }

    public void run() {
        logger.info("Сервер запущен по адресу {}", getAddr());

        while (running) {
            try {
                listenToClients();
            } catch (IOException e) {
                logger.error("Ошибка получения данных : " + e.toString(), e);
                disconnectFromClient();
            }
        }
        close();
    }
}