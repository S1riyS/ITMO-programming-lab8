package s1riys.lab8.common.network;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static s1riys.lab8.common.constants.Network.*;

public class UDPShared {
    private final InetSocketAddress addr;

    public UDPShared(InetAddress address, int port) {
        this.addr = new InetSocketAddress(address, port);
    }

    public InetSocketAddress getAddr() {
        return addr;
    }

    protected byte[][] generateDataChuncks(byte[] data) {
        int chunksNumber = (int) Math.ceil(data.length / (double) DATA_SIZE);
        byte[][] dataChuncks = new byte[chunksNumber][DATA_SIZE];

        int currentIndex = 0;
        for (int i = 0; i < dataChuncks.length; i++) {
            dataChuncks[i] = Arrays.copyOfRange(data, currentIndex, currentIndex + DATA_SIZE);
            currentIndex += DATA_SIZE;
        }
        return dataChuncks;
    }
}
