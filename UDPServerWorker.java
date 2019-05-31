import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.Callable;

public class UDPServerWorker implements Callable<List<QuestaoAcertosErros>> {
  private final DatagramPacket packet;
  private final Map<Integer, List<String>> expectedAnwsers;

  public UDPServerWorker(DatagramPacket requestPacket, Map<Integer, List<String>> gabarito) {
    this.packet = requestPacket;
    this.expectedAnwsers = gabarito;
  }

  @Override
  public List<QuestaoAcertosErros> call() throws Exception {
    try {
      final String recievedString = new String(
        packet.getData(), 0, packet.getData().length
      );
      final Map<Integer, List<String>> parsedAnwser = parseAnwser(recievedString);
      final List<QuestaoAcertosErros> questaoAcertosErros = checkAnwsers(parsedAnwser );
      sendClientAwnser(questaoAcertosErros);
      return questaoAcertosErros;

    } catch (Exception e) {
      System.out.println("Algo de errado aconteceu :(");
    }
    return null;
  }

  private void sendClientAwnser(final List<QuestaoAcertosErros> questaoAcertosErros) throws IOException {
    final StringJoiner respostaFinal = new StringJoiner(";", "", "");
    for (QuestaoAcertosErros questao: questaoAcertosErros) {
      respostaFinal.add(questao.toString());
    }

    final byte[] data = respostaFinal.toString().getBytes();
    DatagramPacket finalPacket = new DatagramPacket(
      data, data.length, this.packet.getAddress(), this.packet.getPort()
    );

    // send datagram as anwser to client
    final DatagramSocket dataSocket = new DatagramSocket();
    dataSocket.send(finalPacket);
    dataSocket.close();
  }

  private List<QuestaoAcertosErros> checkAnwsers(final Map<Integer,List<String>> parsedAnwser) {
    final List<QuestaoAcertosErros> checkedAnwsers = new ArrayList<>();

    for (Map.Entry<Integer, List<String>> entry : expectedAnwsers.entrySet()) {
      final Integer questionNumber = entry.getKey();
      final List<String> questionRigthAnwsers = entry.getValue();

      final List<String> anwsers = parsedAnwser.get(questionNumber);

      Integer mistakes = 0;
      Integer matches = 0;

      for(int i = 0; i < questionRigthAnwsers.size(); i++) {
        if (i > (anwsers.size() - 1)) {
          mistakes++;
          continue;
        }
        final Optional<String> optAnwser = Optional.of(anwsers.get(i));

        if (!optAnwser.isPresent()) {
          mistakes++;
          continue;
        }

        if (optAnwser.get().equals(questionRigthAnwsers.get(i))) {
          matches++;
        } else {
          mistakes++;
        }
      }
      checkedAnwsers.add(
        new QuestaoAcertosErros(questionNumber, matches, mistakes)
      );
    }

    return  checkedAnwsers;
  }

  private Map<Integer, List<String>> parseAnwser(final String data) {
    // 1;5;VVFFV;2;4;VVVV;3;5;FFVFF;4;3;FFF;5;5;VVFVF
    final List<String> splitted = Arrays.asList(data.split(";"));

    assureAnwserIsValid(splitted);

    Map<Integer, List<String>> anwserMap = new HashMap<>();
    for(int i = 0; i < splitted.size(); i += 3) {
      Integer questionNumber = Integer.valueOf(splitted.get(i));
      String anwser = splitted.get(i+2);

      final List<String> anwserSplitted = Arrays.asList(anwser.toUpperCase().trim().split(""));
      anwserMap.put(questionNumber, anwserSplitted);
    }

    return anwserMap;
  }

  private void assureAnwserIsValid(List<String> splitted) {
    int mod = splitted.size() % 3;
    if (mod != 0) {
      throw new RuntimeException("Anwser is incomplete or invalid");
    }
  }
}
