package s1riys.lab8.common.constants;

public interface Network {
    Integer PORT = 21337;
    Integer PACKET_SIZE = 128;
    Integer DATA_SIZE = PACKET_SIZE - 1;
    byte STOP_BYTE = (byte) '>';
    byte CONTINUE_BYTE = (byte) '#';
}
