package com.simuladordepix;

import java.io.IOException;

public interface Servidor {

  Conexao abrirConexao() throws IOException;
}
