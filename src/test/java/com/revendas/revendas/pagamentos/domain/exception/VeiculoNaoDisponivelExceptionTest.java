package com.revendas.revendas.pagamentos.domain.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class VeiculoNaoDisponivelExceptionTest {

    @Test
    void deveCriarMensagemComId() {
        var ex = new VeiculoNaoDisponivelException(5L);
        assertThat(ex.getMessage()).isEqualTo("Veículo com id 5 não está disponível para venda.");
    }

    @Test
    void deveSerRuntimeException() {
        assertThat(new VeiculoNaoDisponivelException(1L)).isInstanceOf(RuntimeException.class);
    }
}
