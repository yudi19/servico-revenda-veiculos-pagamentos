package com.revendas.revendas.pagamentos.adapter.out.persistence;

import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;
import com.revendas.revendas.pagamentos.domain.model.Venda;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class VendaPersistenceMapperTest {

    private static final LocalDate DATA = LocalDate.of(2026, 3, 14);

    private Venda venda() {
        return Venda.builder()
                .id(1L).veiculoId(10L).marcaVeiculo("Toyota").modeloVeiculo("Corolla")
                .anoVeiculo(2023).precoVeiculo(new BigDecimal("120000.00"))
                .cpfComprador("529.982.247-25").dataVenda(DATA)
                .codigoPagamento("uuid-abc").statusPagamento(StatusPagamento.PENDENTE)
                .build();
    }

    private VendaJpaEntity entity() {
        return VendaJpaEntity.builder()
                .id(1L).veiculoId(10L).marcaVeiculo("Toyota").modeloVeiculo("Corolla")
                .anoVeiculo(2023).precoVeiculo(new BigDecimal("120000.00"))
                .cpfComprador("529.982.247-25").dataVenda(DATA)
                .codigoPagamento("uuid-abc").statusPagamento(StatusPagamento.PENDENTE)
                .build();
    }

    @Test
    void toDomain_deveMapearTodosOsCampos() {
        Venda result = VendaPersistenceMapper.toDomain(entity());

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getVeiculoId()).isEqualTo(10L);
        assertThat(result.getMarcaVeiculo()).isEqualTo("Toyota");
        assertThat(result.getModeloVeiculo()).isEqualTo("Corolla");
        assertThat(result.getAnoVeiculo()).isEqualTo(2023);
        assertThat(result.getPrecoVeiculo()).isEqualByComparingTo("120000.00");
        assertThat(result.getCpfComprador()).isEqualTo("529.982.247-25");
        assertThat(result.getDataVenda()).isEqualTo(DATA);
        assertThat(result.getCodigoPagamento()).isEqualTo("uuid-abc");
        assertThat(result.getStatusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
    }

    @Test
    void toEntity_deveMapearTodosOsCampos() {
        VendaJpaEntity result = VendaPersistenceMapper.toEntity(venda());

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getVeiculoId()).isEqualTo(10L);
        assertThat(result.getMarcaVeiculo()).isEqualTo("Toyota");
        assertThat(result.getModeloVeiculo()).isEqualTo("Corolla");
        assertThat(result.getAnoVeiculo()).isEqualTo(2023);
        assertThat(result.getPrecoVeiculo()).isEqualByComparingTo("120000.00");
        assertThat(result.getCpfComprador()).isEqualTo("529.982.247-25");
        assertThat(result.getDataVenda()).isEqualTo(DATA);
        assertThat(result.getCodigoPagamento()).isEqualTo("uuid-abc");
        assertThat(result.getStatusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
    }
}
