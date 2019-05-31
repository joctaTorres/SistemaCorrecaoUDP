import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

public class UDPServer {
  public static void main(String a[]) throws Exception {
    DatagramSocket dataSocket = new DatagramSocket(
      UDPServerConfig.DEFAULT_PORT
    );

    final List<QuestaoAcertosErros> totalResults = new ArrayList<>();

    while (true) {
      final byte[] message =
        new byte[UDPServerConfig.DEFAULT_PACKET_SIZE];

      final DatagramPacket receivingPacket = new DatagramPacket(
        message, message.length
      );
      dataSocket.receive(receivingPacket);

      final UDPServerWorker udpServerWorker = new UDPServerWorker(receivingPacket);

      FutureTask<List<QuestaoAcertosErros>> futureTask = new FutureTask<>(udpServerWorker);
      Thread thread = new Thread(futureTask);
      thread.start();

      List<QuestaoAcertosErros> result = futureTask.get();
      totalResults.addAll(result);
      processResult(totalResults);
    }
  }

  private static void processResult(
    final List<QuestaoAcertosErros> totalResults
  ) {
    final Map<Integer, Integer> acertosMap = totalResults.stream()
      .collect(
        Collectors.groupingBy(q -> q.numeroQuestao, Collectors.summingInt(q -> q.acertos))
      );

    final Map<Integer, Integer> errosMap = totalResults.stream()
      .collect(
        Collectors.groupingBy(q -> q.numeroQuestao, Collectors.summingInt(q -> q.erros))
      );

    System.out.println("Estatística de acertos e erros por questão atualizado:");
    for (Map.Entry<Integer, Integer> entry : acertosMap.entrySet()) {
      final Integer questao = entry.getKey();

      System.out.println(
        new QuestaoAcertosErros(questao, acertosMap.get(questao), errosMap.get(questao))
          .getResumoString()
      );
    }
  }
}

