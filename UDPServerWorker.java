import java.net.*;
import java.io.*;

public class UDPServerWorker extends Thread {
  private final DatagramPacket packet;

  public UDPServerWorker(DatagramPacket requestPacket) {
    this.packet = new DatagramPacket(
      requestPacket.getData(),
      requestPacket.getLength(),
      requestPacket.getAddress(),
      requestPacket.getPort()
    );
  }

  public void run() {
    try {
      final DatagramSocket dataSocket = new DatagramSocket();
      dataSocket.send(this.packet);
      dataSocket.close();
    } catch (Exception e) {
      System.out.println("Algo de errado aconteceu :(");
    }
  }

  private 
}
