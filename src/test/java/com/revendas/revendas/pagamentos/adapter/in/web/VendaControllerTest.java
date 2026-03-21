package com.revendas.revendas.pagamentos.adapter.in.web;

import com.revendas.revendas.pagamentos.application.port.in.EfetuarVendaUseCase;
import com.revendas.revendas.pagamentos.application.port.in.ListarVendidasUseCase;
import com.revendas.revendas.pagamentos.application.port.in.ProcessarWebhookUseCase;
import com.revendas.revendas.pagamentos.domain.exception.VeiculoNaoDisponivelException;
import com.revendas.revendas.pagamentos.domain.exception.VendaNaoEncontradaException;
import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;
import com.revendas.revendas.pagamentos.domain.model.Venda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VendaControllerTest {

    @Mock
    private EfetuarVendaUseCase efetuarVendaUseCase;

    @Mock
    private ProcessarWebhookUseCase processarWebhookUseCase;

    @Mock
    private ListarVendidasUseCase listarVendidasUseCase;

    @InjectMocks
    private VendaController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private Venda vendaBase() {
        return Venda.builder()
                .id(1L)
                .veiculoId(10L)
                .marcaVeiculo("Toyota")
                .modeloVeiculo("Corolla")
                .anoVeiculo(2023)
                .precoVeiculo(new BigDecimal("120000.00"))
                .cpfComprador("529.982.247-25")
                .dataVenda(LocalDate.of(2024, 1, 1))
                .codigoPagamento("uuid-123")
                .statusPagamento(StatusPagamento.PENDENTE)
                .build();
    }

    // --- POST /vendas ---

    @Test
    void efetuarVenda_deveRetornar201ComVenda() throws Exception {
        when(efetuarVendaUseCase.efetuarVenda(any(), any(), any())).thenReturn(vendaBase());

        String json = "{\"veiculoId\":10,\"cpfComprador\":\"529.982.247-25\",\"dataVenda\":\"2024-01-01\"}";

        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigoPagamento").value("uuid-123"))
                .andExpect(jsonPath("$.statusPagamento").value("PENDENTE"));
    }

    @Test
    void efetuarVenda_deveRetornar409QuandoVeiculoNaoDisponivel() throws Exception {
        when(efetuarVendaUseCase.efetuarVenda(any(), any(), any()))
                .thenThrow(new VeiculoNaoDisponivelException(10L));

        String json = "{\"veiculoId\":10,\"cpfComprador\":\"529.982.247-25\",\"dataVenda\":\"2024-01-01\"}";

        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void efetuarVenda_deveRetornar404QuandoVeiculoNaoEncontrado() throws Exception {
        when(efetuarVendaUseCase.efetuarVenda(any(), any(), any()))
                .thenThrow(HttpClientErrorException.NotFound.create(
                        HttpStatus.NOT_FOUND, "Not Found",
                        HttpHeaders.EMPTY, new byte[0], null));

        String json = "{\"veiculoId\":99,\"cpfComprador\":\"529.982.247-25\",\"dataVenda\":\"2024-01-01\"}";

        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void efetuarVenda_deveRetornar400ComCpfInvalido() throws Exception {
        String json = "{\"veiculoId\":10,\"cpfComprador\":\"000.000.000-00\",\"dataVenda\":\"2024-01-01\"}";

        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void efetuarVenda_deveRetornar400QuandoCamposObrigatoriosAusentes() throws Exception {
        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // --- POST /vendas/webhook ---

    @Test
    void processarWebhook_deveRetornar200() throws Exception {
        when(processarWebhookUseCase.processar(anyString(), any())).thenReturn(vendaBase());

        String json = "{\"codigoPagamento\":\"uuid-123\",\"status\":\"APROVADO\"}";

        mockMvc.perform(post("/vendas/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoPagamento").value("uuid-123"));
    }

    @Test
    void processarWebhook_deveRetornar404QuandoNaoEncontrada() throws Exception {
        when(processarWebhookUseCase.processar(anyString(), any()))
                .thenThrow(new VendaNaoEncontradaException("uuid-999"));

        String json = "{\"codigoPagamento\":\"uuid-999\",\"status\":\"APROVADO\"}";

        mockMvc.perform(post("/vendas/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void processarWebhook_deveRetornar400QuandoBodyInvalido() throws Exception {
        mockMvc.perform(post("/vendas/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // --- GET /vendas/vendidos ---

    @Test
    void listarVendidos_deveRetornar200ComLista() throws Exception {
        when(listarVendidasUseCase.listarVendidas()).thenReturn(List.of(vendaBase()));

        mockMvc.perform(get("/vendas/vendidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigoPagamento").value("uuid-123"));
    }

    @Test
    void listarVendidos_deveRetornar200ComListaVazia() throws Exception {
        when(listarVendidasUseCase.listarVendidas()).thenReturn(List.of());

        mockMvc.perform(get("/vendas/vendidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
