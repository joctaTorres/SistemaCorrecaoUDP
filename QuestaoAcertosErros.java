public class QuestaoAcertosErros {
  public final Integer numeroQuestao;
  public Integer acertos;
  public Integer erros;

  public QuestaoAcertosErros(final Integer numeroQuestao, final Integer acertos, final Integer erros) {
    this.numeroQuestao = numeroQuestao;
    this.acertos = acertos;
    this.erros = erros;
  }

  public int getNumeroQuestao() {
    return this.numeroQuestao;
  }

  public void aumentarAcerto(final int aumento) {
    this.acertos += aumento;
  }

  public void aumentarErros(final int aumento) {
    this.erros += aumento;
  }

  public String getResumoString() {
    return String.format("Questão %d: acertos = %d erros = %d", numeroQuestao, acertos, erros);
  }

  @Override
  public String toString() {
    return String.format("%d;%d;%d", numeroQuestao, acertos, erros);
  }
}
