package com.revendas.revendas.pagamentos.adapter.out.http;

import com.revendas.revendas.pagamentos.application.port.out.VeiculoInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoHttpAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private VeiculoHttpAdapter adapter;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(adapter, "listagemServiceUrl", "http://localhost:8080");
    }

    private VeiculoClientResponse responseBase() {
        return new VeiculoClientResponse(1L, "Toyota", "Corolla", 2023,
                "Branco", new BigDecimal("120000.00"), "ABC-1234", "DISPONIVEL");
    }

    // --- buscarVeiculo ---

    @Test
    void buscarVeiculo_deveRetornarVeiculoInfo() {
        when(restTemplate.getForObject("http://localhost:8080/veiculos/1", VeiculoClientResponse.class))
                .thenReturn(responseBase());

        VeiculoInfo result = adapter.buscarVeiculo(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.marca()).isEqualTo("Toyota");
        assertThat(result.modelo()).isEqualTo("Corolla");
        assertThat(result.ano()).isEqualTo(2023);
        assertThat(result.preco()).isEqualByComparingTo("120000.00");
        assertThat(result.status()).isEqualTo("DISPONIVEL");
    }

    @Test
    void buscarVeiculo_deveLancarExcecaoQuandoRespostaNula() {
        when(restTemplate.getForObject(anyString(), eq(VeiculoClientResponse.class)))
                .thenReturn(null);

        assertThatThrownBy(() -> adapter.buscarVeiculo(99L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("99");
    }

    // --- atualizarStatusVeiculo ---

    @Test
    void atualizarStatusVeiculo_deveEnviarPutComNovoStatus() {
        when(restTemplate.getForObject("http://localhost:8080/veiculos/1", VeiculoClientResponse.class))
                .thenReturn(responseBase());
        doNothing().when(restTemplate).put(anyString(), any(HttpEntity.class));

        adapter.atualizarStatusVeiculo(1L, "VENDIDO");

        verify(restTemplate).put(eq("http://localhost:8080/veiculos/1"), any(HttpEntity.class));
    }

    @Test
    void atualizarStatusVeiculo_deveLancarExcecaoQuandoRespostaNula() {
        when(restTemplate.getForObject(anyString(), eq(VeiculoClientResponse.class)))
                .thenReturn(null);

        assertThatThrownBy(() -> adapter.atualizarStatusVeiculo(99L, "VENDIDO"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("99");
    }
}
