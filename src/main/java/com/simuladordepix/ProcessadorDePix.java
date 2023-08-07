package com.simuladordepix;

import java.io.IOException;

public class ProcessadorDePix {

  private final Servidor servidor;

  public ProcessadorDePix(Servidor servidor) {
    this.servidor = servidor;
  }

  /**
   * Executa a operação do pix. Aqui é implementada a lógica de negócio sem envolver as interações
   * do aplicativo com a pessoa usuária.
   *
   * @param valor Valor em centavos a ser transferido.
   * @param chave Chave Pix do beneficiário da transação.
   * @throws ErroDePix   Erro de aplicação, caso ocorra qualquer inconformidade.
   * @throws IOException Caso aconteça algum problema relacionado à comunicação entre o aplicativo e
   *                     o servidor na nuvem.
   */
  public void executarPix(int valor, String chave) throws ErroDePix, IOException {
    if (valor < 1) {
      throw new ErroValorNaoPositivo();
    }
    if (chave.trim().isEmpty()) {
      throw new ErroChaveEmBranco();
    }

    try (Conexao conexao = this.servidor.abrirConexao()) {
      String codigoDeRetorno = conexao.enviarPix(valor, chave);
      this.validarCodigoDeRetorno(codigoDeRetorno);
    }
  }

  private void validarCodigoDeRetorno(String codigoDeRetorno)
      throws ErroSaldoInsuficiente, ErroChaveNaoEncontrada, ErroInterno {
    if (codigoDeRetorno.equals(CodigosDeRetorno.SALDO_INSUFICIENTE)) {
      throw new ErroSaldoInsuficiente();
    }
    if (codigoDeRetorno.equals(CodigosDeRetorno.CHAVE_PIX_NAO_ENCONTRADA)) {
      throw new ErroChaveNaoEncontrada();
    }
    if (!codigoDeRetorno.equals(CodigosDeRetorno.SUCESSO)) {
      throw new ErroInterno();
    }
  }
}
