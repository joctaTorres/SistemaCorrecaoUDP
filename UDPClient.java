import java.net.*;
import java.io.*;

public class UDPClient {
  public static void main(String a[]) throws Exception {
    DatagramSocket dataSocket = new DatagramSocket();

    byte[] msg = a[0].getBytes();

    InetAddress endDst = InetAddress.getByName("localhost");
    int portDst = UDPServerConfig.DEFAULT_PORT;

    DatagramPacket packet = new DatagramPacket(
      msg, msg.length, endDst, portDst
    );

    System.out.println(
      String.format(
        "Enviando respostas: %s para a correção.",
        a[0]
      )
    );

    dataSocket.send(packet);

    byte[] receivingMsg = new byte[UDPServerConfig.DEFAULT_PACKET_SIZE];
    DatagramPacket receivingPacket = new DatagramPacket(
      receivingMsg,
      receivingMsg.length
    );
    dataSocket.receive(receivingPacket);

    final String receivedData = new String(receivingPacket.getData());
    System.out.println("Resultado da correção: " + receivedData);
  }
}

