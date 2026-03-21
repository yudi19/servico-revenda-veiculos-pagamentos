package com.revendas.revendas.pagamentos.application.service;

import com.revendas.revendas.pagamentos.application.port.out.VeiculoInfo;
import com.revendas.revendas.pagamentos.application.port.out.VeiculoServicePort;
import com.revendas.revendas.pagamentos.application.port.out.VendaRepositoryPort;
import com.revendas.revendas.pagamentos.domain.exception.VeiculoNaoDisponivelException;
import com.revendas.revendas.pagamentos.domain.exception.VendaNaoEncontradaException;
import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;
import com.revendas.revendas.pagamentos.domain.model.Venda;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepositoryPort vendaRepositoryPort;

    @Mock
    private VeiculoServicePort veiculoServicePort;

    @InjectMocks
    private VendaService service;

    private VeiculoInfo veiculoDisponivel() {
        return new VeiculoInfo(1L, "Toyota", "Corolla", 2023,
                new BigDecimal("120000.00"), "DISPONIVEL");
    }

    private VeiculoInfo veiculoReservado() {
        return new VeiculoInfo(1L, "Toyota", "Corolla", 2023,
                new BigDecimal("120000.00"), "RESERVADO");
    }

    private Venda vendaPendente() {
        return Venda.builder()
                .id(1L).veiculoId(1L).marcaVeiculo("Toyota").modeloVeiculo("Corolla")
                .anoVeiculo(2023).precoVeiculo(new BigDecimal("120000.00"))
                .cpfComprador("529.982.247-25").dataVenda(LocalDate.of(2026, 3, 14))
                .codigoPagamento("uuid-abc").statusPagamento(StatusPagamento.PENDENTE)
                .build();
    }

    // --- efetuarVenda ---

    @Test
    void efetuarVenda_deveCriarVendaQuandoVeiculoDisponivel() {
        when(veiculoServicePort.buscarVeiculo(1L)).thenReturn(veiculoDisponivel());
        when(vendaRepositoryPort.salvar(any())).thenReturn(vendaPendente());

        Venda result = service.efetuarVenda(1L, "529.982.247-25", LocalDate.of(2026, 3, 14));

        assertThat(result.getStatusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
        assertThat(result.getCpfComprador()).isEqualTo("529.982.247-25");
        verify(veiculoServicePort).atualizarStatusVeiculo(1L, "RESERVADO");
        verify(vendaRepositoryPort).salvar(any());
    }

    @Test
    void efetuarVenda_deveLancarExcecaoQuandoVeiculoNaoDisponivel() {
        when(veiculoServicePort.buscarVeiculo(1L)).thenReturn(veiculoReservado());

        assertThatThrownBy(() -> service.efetuarVenda(1L, "529.982.247-25", LocalDate.now()))
                .isInstanceOf(VeiculoNaoDisponivelException.class)
                .hasMessageContaining("1");

        verify(vendaRepositoryPort, never()).salvar(any());
        verify(veiculoServicePort, never()).atualizarStatusVeiculo(any(), any());
    }

    // --- processar (webhook) ---

    @Test
    void processar_APROVADO_deveAtualizarStatusEVeiculo() {
        Venda venda = vendaPendente();
        when(vendaRepositoryPort.buscarPorCodigoPagamento("uuid-abc"))
                .thenReturn(Optional.of(venda));
        when(vendaRepositoryPort.salvar(venda)).thenReturn(venda);

        Venda result = service.processar("uuid-abc", StatusPagamento.APROVADO);

        assertThat(result.getStatusPagamento()).isEqualTo(StatusPagamento.APROVADO);
        verify(veiculoServicePort).atualizarStatusVeiculo(1L, "VENDIDO");
    }

    @Test
    void processar_CANCELADO_deveAtualizarStatusEVeiculo() {
        Venda venda = vendaPendente();
        when(vendaRepositoryPort.buscarPorCodigoPagamento("uuid-abc"))
                .thenReturn(Optional.of(venda));
        when(vendaRepositoryPort.salvar(venda)).thenReturn(venda);

        Venda result = service.processar("uuid-abc", StatusPagamento.CANCELADO);

        assertThat(result.getStatusPagamento()).isEqualTo(StatusPagamento.CANCELADO);
        verify(veiculoServicePort).atualizarStatusVeiculo(1L, "DISPONIVEL");
    }

    @Test
    void processar_PENDENTE_naoDeveAtualizarVeiculo() {
        Venda venda = vendaPendente();
        when(vendaRepositoryPort.buscarPorCodigoPagamento("uuid-abc"))
                .thenReturn(Optional.of(venda));
        when(vendaRepositoryPort.salvar(venda)).thenReturn(venda);

        service.processar("uuid-abc", StatusPagamento.PENDENTE);

        verify(veiculoServicePort, never()).atualizarStatusVeiculo(any(), any());
    }

    @Test
    void processar_deveLancarExcecaoQuandoCodigoInexistente() {
        when(vendaRepositoryPort.buscarPorCodigoPagamento("nao-existe"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.processar("nao-existe", StatusPagamento.APROVADO))
                .isInstanceOf(VendaNaoEncontradaException.class)
                .hasMessageContaining("nao-existe");
    }

    // --- listarVendidas ---

    @Test
    void listarVendidas_deveRetornarListaDoRepositorio() {
        List<Venda> lista = List.of(vendaPendente());
        when(vendaRepositoryPort.listarAprovadas()).thenReturn(lista);

        List<Venda> result = service.listarVendidas();

        assertThat(result).hasSize(1);
        verify(vendaRepositoryPort).listarAprovadas();
    }

    @Test
    void listarVendidas_deveRetornarListaVaziaQuandoNaoHaVendas() {
        when(vendaRepositoryPort.listarAprovadas()).thenReturn(List.of());

        assertThat(service.listarVendidas()).isEmpty();
    }
}
