import java.net.*;
import java.io.*;

public class UDPServer3 {
  public static void main(String a[]) throws Exception {
    DatagramSocket dataSocket = new DatagramSocket(6789);

    while (true) {
      byte[] message = new byte[1024];
      DatagramPacket receivingPacket = new DatagramPacket(message, message.length);
      dataSocket.receive(receivingPacket);

      new UDPServer3WorkerThread(receivingPacket).start();
    }
  }
}
