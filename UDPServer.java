import java.net.*;
import java.io.*;

public class UDPServer3 {
  public static void main(String a[]) throws Exception {
    DatagramSocket dataSocket = new DatagramSocket(
      UDPServerConfig.DEFAULT_PORT
    );

    while (true) {
      final byte[] message =
        new byte[UDPServerConfig.DEFAULT_PACKET_SIZE];

      final DatagramPacket receivingPacket = new DatagramPacket(
        message, message.length
      );
      dataSocket.receive(receivingPacket);

      new UDPServer3WorkerThread(receivingPacket).start();
    }
  }
}
