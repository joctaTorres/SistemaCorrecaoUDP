import java.net.*;
import java.io.*;

public class UDPClient {
	public static void main (String a[]) throws Exception {
		DatagramSocket dataSocket = new DatagramSocket();

		byte[] msg = a[0].getBytes();
		InetAddress endDst = InetAddress.getByName(a[1]);
		int portDst = 6789;

		DatagramPacket packet = new DatagramPacket(msg, msg.length, endDst, portDst);
        System.out.println(String.format("Enviando o pacote UDP para o endereço %s, porta %d", a[1], portDst));
		dataSocket.send(packet);

		byte[] receivingMsg = new byte[1024];
		DatagramPacket receivingPacket = new DatagramPacket(receivingMsg, receivingMsg.length);
		dataSocket.receive(receivingPacket);

		System.out.println("Chegou: " + new String(receivingPacket.getData()));
	}

}

