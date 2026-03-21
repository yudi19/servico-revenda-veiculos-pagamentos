package com.revendas.revendas.pagamentos.adapter.out.http;

import com.revendas.revendas.pagamentos.application.port.out.VeiculoInfo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class VeiculoClientRecordsTest {

    @Test
    void veiculoClientResponse_deveExporsAcessores() {
        VeiculoClientResponse r = new VeiculoClientResponse(
                1L, "Toyota", "Corolla", 2023,
                "Branco", new BigDecimal("120000"), "ABC-1234", "DISPONIVEL");
        assertThat(r.id()).isEqualTo(1L);
        assertThat(r.marca()).isEqualTo("Toyota");
        assertThat(r.modelo()).isEqualTo("Corolla");
        assertThat(r.ano()).isEqualTo(2023);
        assertThat(r.cor()).isEqualTo("Branco");
        assertThat(r.preco()).isEqualByComparingTo("120000");
        assertThat(r.placa()).isEqualTo("ABC-1234");
        assertThat(r.status()).isEqualTo("DISPONIVEL");
    }

    @Test
    void veiculoClientRequest_deveExporsAcessores() {
        VeiculoClientRequest r = new VeiculoClientRequest(
                "Toyota", "Corolla", 2023,
                "Branco", new BigDecimal("120000"), "ABC-1234", "VENDIDO");
        assertThat(r.marca()).isEqualTo("Toyota");
        assertThat(r.modelo()).isEqualTo("Corolla");
        assertThat(r.ano()).isEqualTo(2023);
        assertThat(r.cor()).isEqualTo("Branco");
        assertThat(r.preco()).isEqualByComparingTo("120000");
        assertThat(r.placa()).isEqualTo("ABC-1234");
        assertThat(r.status()).isEqualTo("VENDIDO");
    }

    @Test
    void veiculoInfo_deveExporsAcessores() {
        VeiculoInfo info = new VeiculoInfo(
                1L, "Toyota", "Corolla", 2023,
                new BigDecimal("120000"), "DISPONIVEL");
        assertThat(info.id()).isEqualTo(1L);
        assertThat(info.marca()).isEqualTo("Toyota");
        assertThat(info.modelo()).isEqualTo("Corolla");
        assertThat(info.ano()).isEqualTo(2023);
        assertThat(info.preco()).isEqualByComparingTo("120000");
        assertThat(info.status()).isEqualTo("DISPONIVEL");
    }
}
