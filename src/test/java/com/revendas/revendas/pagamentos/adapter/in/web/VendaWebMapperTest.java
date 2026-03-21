package com.revendas.revendas.pagamentos.adapter.in.web;

import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;
import com.revendas.revendas.pagamentos.domain.model.Venda;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class VendaWebMapperTest {

    @Test
    void toResponse_deveMapearTodosOsCampos() {
        Venda venda = Venda.builder()
                .id(1L).veiculoId(10L).marcaVeiculo("Toyota").modeloVeiculo("Corolla")
                .anoVeiculo(2023).precoVeiculo(new BigDecimal("120000.00"))
                .cpfComprador("529.982.247-25").dataVenda(LocalDate.of(2026, 3, 14))
                .codigoPagamento("uuid-abc").statusPagamento(StatusPagamento.PENDENTE)
                .build();

        VendaResponseDTO result = VendaWebMapper.toResponse(venda);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.veiculoId()).isEqualTo(10L);
        assertThat(result.marcaVeiculo()).isEqualTo("Toyota");
        assertThat(result.modeloVeiculo()).isEqualTo("Corolla");
        assertThat(result.anoVeiculo()).isEqualTo(2023);
        assertThat(result.precoVeiculo()).isEqualByComparingTo("120000.00");
        assertThat(result.cpfComprador()).isEqualTo("529.982.247-25");
        assertThat(result.dataVenda()).isEqualTo(LocalDate.of(2026, 3, 14));
        assertThat(result.codigoPagamento()).isEqualTo("uuid-abc");
        assertThat(result.statusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
    }
}
