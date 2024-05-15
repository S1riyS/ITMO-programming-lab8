package s1riys.lab8.client.network;

import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.Logger;
import s1riys.lab8.client.Main;
import s1riys.lab8.common.network.UDPShared;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.Response;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

import static s1riys.lab8.common.constants.Network.*;

public class UDPClient extends UDPShared {
    private final DatagramChannel client;
    private final Logger logger = Main.logger;

    public UDPClient(InetAddress address, int port) throws IOException {
        super(address, port);
        this.client = DatagramChannel.open().bind(null).connect(getAddr());
        this.client.configureBlocking(false);
        logger.info("DatagramChannel подключен к {}", getAddr());
    }

    public Response sendAndReceiveCommand(Request request) throws IOException {
        byte[] data = SerializationUtils.serialize(request);
        byte[] responseBytes = sendAndReceiveData(data);

        Response response = SerializationUtils.deserialize(responseBytes);
        logger.info("Получен ответ от сервера: {}", response);
        return response;
    }

    private void sendData(byte[] data) throws IOException {
        byte[][] dataChuncks = this.generateDataChuncks(data);

        logger.info("Отправление чанков... ({} шт.)", dataChuncks.length);
        for (int i = 0; i < dataChuncks.length; i++) {
            var chunk = dataChuncks[i];

            if (i == dataChuncks.length - 1) {
                var lastChunk = Bytes.concat(chunk, new byte[]{STOP_BYTE});
                client.send(ByteBuffer.wrap(lastChunk), getAddr());
                logger.info("Последний чанк отправлен на сервер.");
            } else {
                var intermediateChunck = Bytes.concat(chunk, new byte[]{CONTINUE_BYTE});
                client.send(ByteBuffer.wrap(intermediateChunck), getAddr());
            }
        }

        logger.info("Отправка данных завершена.");
    }

    private byte[] receiveData() throws IOException {
        var received = false;
        var result = new byte[0];

        while (!received) {
            var data = receiveDataChunk(PACKET_SIZE);
            logger.info("Получено \"{}\"", new String(data));
            logger.info("Последний байт: {}", String.valueOf(data[data.length - 1]));

            if (data[data.length - 1] == STOP_BYTE) {
                received = true;
                logger.info("Получение данных завершено");
            }
            result = Bytes.concat(result, Arrays.copyOf(data, data.length - 1));
        }

        return result;
    }

    private byte[] receiveDataChunk(int bufferSize) throws IOException {
        var buffer = ByteBuffer.allocate(bufferSize);
        SocketAddress address = null;
        while (address == null) {
            address = client.receive(buffer);
        }
        return buffer.array();
    }

    private byte[] sendAndReceiveData(byte[] data) throws IOException {
        sendData(data);
        return receiveData();
    }
}
