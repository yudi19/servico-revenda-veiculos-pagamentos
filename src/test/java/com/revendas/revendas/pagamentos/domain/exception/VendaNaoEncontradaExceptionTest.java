package com.revendas.revendas.pagamentos.domain.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class VendaNaoEncontradaExceptionTest {

    @Test
    void deveCriarMensagemComCodigo() {
        var ex = new VendaNaoEncontradaException("codigo-abc");
        assertThat(ex.getMessage())
                .isEqualTo("Venda não encontrada para o código de pagamento: codigo-abc");
    }

    @Test
    void deveSerRuntimeException() {
        assertThat(new VendaNaoEncontradaException("x")).isInstanceOf(RuntimeException.class);
    }
}
