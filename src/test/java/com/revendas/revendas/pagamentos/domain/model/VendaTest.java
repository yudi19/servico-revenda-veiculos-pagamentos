package com.revendas.revendas.pagamentos.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class VendaTest {

    @Test
    void deveBuildarVendaComTodosOsCampos() {
        Venda v = Venda.builder()
                .id(1L).veiculoId(10L).marcaVeiculo("Toyota").modeloVeiculo("Corolla")
                .anoVeiculo(2023).precoVeiculo(new BigDecimal("120000.00"))
                .cpfComprador("529.982.247-25").dataVenda(LocalDate.of(2026, 3, 14))
                .codigoPagamento("uuid-abc").statusPagamento(StatusPagamento.PENDENTE)
                .build();

        assertThat(v.getId()).isEqualTo(1L);
        assertThat(v.getVeiculoId()).isEqualTo(10L);
        assertThat(v.getMarcaVeiculo()).isEqualTo("Toyota");
        assertThat(v.getModeloVeiculo()).isEqualTo("Corolla");
        assertThat(v.getAnoVeiculo()).isEqualTo(2023);
        assertThat(v.getPrecoVeiculo()).isEqualByComparingTo("120000.00");
        assertThat(v.getCpfComprador()).isEqualTo("529.982.247-25");
        assertThat(v.getDataVenda()).isEqualTo(LocalDate.of(2026, 3, 14));
        assertThat(v.getCodigoPagamento()).isEqualTo("uuid-abc");
        assertThat(v.getStatusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
    }

    @Test
    void devePermitirSettersEGetters() {
        Venda v = new Venda();
        v.setId(2L);
        v.setVeiculoId(20L);
        v.setMarcaVeiculo("Honda");
        v.setModeloVeiculo("Civic");
        v.setAnoVeiculo(2022);
        v.setPrecoVeiculo(new BigDecimal("90000.00"));
        v.setCpfComprador("123.456.789-09");
        v.setDataVenda(LocalDate.of(2026, 1, 1));
        v.setCodigoPagamento("uuid-xyz");
        v.setStatusPagamento(StatusPagamento.APROVADO);

        assertThat(v.getId()).isEqualTo(2L);
        assertThat(v.getVeiculoId()).isEqualTo(20L);
        assertThat(v.getStatusPagamento()).isEqualTo(StatusPagamento.APROVADO);
    }

    @Test
    void deveTerEqualsEHashCode() {
        Venda v1 = Venda.builder().id(1L).codigoPagamento("abc").build();
        Venda v2 = Venda.builder().id(1L).codigoPagamento("abc").build();
        assertThat(v1).isEqualTo(v2);
        assertThat(v1.hashCode()).isEqualTo(v2.hashCode());
    }

    @Test
    void deveTerToString() {
        Venda v = Venda.builder().id(1L).marcaVeiculo("Toyota").build();
        assertThat(v.toString()).contains("Toyota");
    }

    @Test
    void deveInstanciarComConstrutorCompleto() {
        Venda v = new Venda(1L, 2L, "Toyota", "Corolla", 2023,
                new BigDecimal("100000"), "529.982.247-25",
                LocalDate.now(), "uuid", StatusPagamento.CANCELADO);
        assertThat(v.getStatusPagamento()).isEqualTo(StatusPagamento.CANCELADO);
    }
}
