package com.revendas.revendas.pagamentos.adapter.out.persistence;

import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class VendaJpaEntityTest {

    private static final LocalDate DATA = LocalDate.of(2026, 3, 14);

    private VendaJpaEntity entity() {
        return VendaJpaEntity.builder()
                .id(1L).veiculoId(10L).marcaVeiculo("Toyota").modeloVeiculo("Corolla")
                .anoVeiculo(2023).precoVeiculo(new BigDecimal("120000.00"))
                .cpfComprador("529.982.247-25").dataVenda(DATA)
                .codigoPagamento("uuid-abc").statusPagamento(StatusPagamento.PENDENTE)
                .build();
    }

    @Test
    void deveBuildarComTodosOsCampos() {
        VendaJpaEntity e = entity();
        assertThat(e.getId()).isEqualTo(1L);
        assertThat(e.getVeiculoId()).isEqualTo(10L);
        assertThat(e.getMarcaVeiculo()).isEqualTo("Toyota");
        assertThat(e.getModeloVeiculo()).isEqualTo("Corolla");
        assertThat(e.getAnoVeiculo()).isEqualTo(2023);
        assertThat(e.getPrecoVeiculo()).isEqualByComparingTo("120000.00");
        assertThat(e.getCpfComprador()).isEqualTo("529.982.247-25");
        assertThat(e.getDataVenda()).isEqualTo(DATA);
        assertThat(e.getCodigoPagamento()).isEqualTo("uuid-abc");
        assertThat(e.getStatusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
    }

    @Test
    void devePermitirSetters() {
        VendaJpaEntity e = new VendaJpaEntity();
        e.setId(2L);
        e.setVeiculoId(20L);
        e.setMarcaVeiculo("Honda");
        e.setModeloVeiculo("Civic");
        e.setAnoVeiculo(2022);
        e.setPrecoVeiculo(new BigDecimal("90000.00"));
        e.setCpfComprador("123.456.789-09");
        e.setDataVenda(LocalDate.now());
        e.setCodigoPagamento("uuid-xyz");
        e.setStatusPagamento(StatusPagamento.APROVADO);

        assertThat(e.getMarcaVeiculo()).isEqualTo("Honda");
        assertThat(e.getStatusPagamento()).isEqualTo(StatusPagamento.APROVADO);
    }

    @Test
    void deveTerEqualsEHashCode() {
        VendaJpaEntity e1 = entity();
        VendaJpaEntity e2 = entity();
        assertThat(e1).isEqualTo(e2);
        assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
    }

    @Test
    void deveTerToString() {
        assertThat(entity().toString()).contains("Toyota");
    }

    @Test
    void deveInstanciarComConstrutorCompleto() {
        VendaJpaEntity e = new VendaJpaEntity(1L, 10L, "Toyota", "Corolla", 2023,
                new BigDecimal("120000"), "529.982.247-25", DATA, "uuid", StatusPagamento.CANCELADO);
        assertThat(e.getStatusPagamento()).isEqualTo(StatusPagamento.CANCELADO);
    }

    @Test
    void privateConstructorDoMapper_devePodeSerInstanciado() throws Exception {
        Constructor<VendaPersistenceMapper> c =
                VendaPersistenceMapper.class.getDeclaredConstructor();
        c.setAccessible(true);
        assertThat(c.newInstance()).isNotNull();
    }
}
