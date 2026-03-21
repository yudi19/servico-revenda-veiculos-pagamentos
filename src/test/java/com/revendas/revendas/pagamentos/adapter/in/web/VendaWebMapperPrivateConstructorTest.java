package com.revendas.revendas.pagamentos.adapter.in.web;

import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class VendaWebMapperPrivateConstructorTest {

    @Test
    void privateConstructor_devePodeSerInstanciado() throws Exception {
        Constructor<VendaWebMapper> c = VendaWebMapper.class.getDeclaredConstructor();
        c.setAccessible(true);
        assertThat(c.newInstance()).isNotNull();
    }

    @Test
    void efetuarVendaRequestDTO_deveExporsAcessores() {
        EfetuarVendaRequestDTO dto = new EfetuarVendaRequestDTO(
                1L, "529.982.247-25", LocalDate.of(2026, 3, 14));
        assertThat(dto.veiculoId()).isEqualTo(1L);
        assertThat(dto.cpfComprador()).isEqualTo("529.982.247-25");
        assertThat(dto.dataVenda()).isEqualTo(LocalDate.of(2026, 3, 14));
    }

    @Test
    void webhookRequestDTO_deveExporsAcessores() {
        WebhookRequestDTO dto = new WebhookRequestDTO("uuid-abc", StatusPagamento.APROVADO);
        assertThat(dto.codigoPagamento()).isEqualTo("uuid-abc");
        assertThat(dto.status()).isEqualTo(StatusPagamento.APROVADO);
    }

    @Test
    void vendaResponseDTO_deveExporsAcessores() {
        VendaResponseDTO dto = new VendaResponseDTO(
                1L, 2L, "Toyota", "Corolla", 2023,
                new BigDecimal("120000"), "529.982.247-25",
                LocalDate.of(2026, 3, 14), "uuid-abc", StatusPagamento.APROVADO);
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.statusPagamento()).isEqualTo(StatusPagamento.APROVADO);
    }
}
