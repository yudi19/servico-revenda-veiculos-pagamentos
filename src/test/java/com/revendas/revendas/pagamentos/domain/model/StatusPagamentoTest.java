package com.revendas.revendas.pagamentos.domain.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class StatusPagamentoTest {

    @Test
    void deveConterTodosOsValores() {
        assertThat(StatusPagamento.values()).containsExactlyInAnyOrder(
                StatusPagamento.PENDENTE,
                StatusPagamento.APROVADO,
                StatusPagamento.CANCELADO
        );
    }

    @Test
    void deveResolverPorNome() {
        assertThat(StatusPagamento.valueOf("PENDENTE")).isEqualTo(StatusPagamento.PENDENTE);
        assertThat(StatusPagamento.valueOf("APROVADO")).isEqualTo(StatusPagamento.APROVADO);
        assertThat(StatusPagamento.valueOf("CANCELADO")).isEqualTo(StatusPagamento.CANCELADO);
    }
}
