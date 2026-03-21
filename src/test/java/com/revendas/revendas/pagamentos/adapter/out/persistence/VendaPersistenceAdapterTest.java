package com.revendas.revendas.pagamentos.adapter.out.persistence;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaPersistenceAdapterTest {

    @Mock
    private VendaJpaRepository jpaRepository;

    @InjectMocks
    private VendaPersistenceAdapter adapter;

    private VendaJpaEntity entityBase() {
        return VendaJpaEntity.builder()
                .id(1L).veiculoId(10L).marcaVeiculo("Toyota").modeloVeiculo("Corolla")
                .anoVeiculo(2023).precoVeiculo(new BigDecimal("120000.00"))
                .cpfComprador("529.982.247-25").dataVenda(LocalDate.of(2026, 3, 14))
                .codigoPagamento("uuid-abc").statusPagamento(StatusPagamento.PENDENTE)
                .build();
    }

    private Venda vendaBase() {
        return Venda.builder()
                .id(1L).veiculoId(10L).marcaVeiculo("Toyota").modeloVeiculo("Corolla")
                .anoVeiculo(2023).precoVeiculo(new BigDecimal("120000.00"))
                .cpfComprador("529.982.247-25").dataVenda(LocalDate.of(2026, 3, 14))
                .codigoPagamento("uuid-abc").statusPagamento(StatusPagamento.PENDENTE)
                .build();
    }

    @Test
    void salvar_deveRetornarDomainMapeado() {
        when(jpaRepository.save(any())).thenReturn(entityBase());

        Venda result = adapter.salvar(vendaBase());

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getMarcaVeiculo()).isEqualTo("Toyota");
        verify(jpaRepository).save(any());
    }

    @Test
    void buscarPorCodigoPagamento_deveRetornarOptionalComVenda() {
        when(jpaRepository.findByCodigoPagamento("uuid-abc"))
                .thenReturn(Optional.of(entityBase()));

        Optional<Venda> result = adapter.buscarPorCodigoPagamento("uuid-abc");

        assertThat(result).isPresent();
        assertThat(result.get().getCodigoPagamento()).isEqualTo("uuid-abc");
    }

    @Test
    void buscarPorCodigoPagamento_deveRetornarOptionalVazioQuandoNaoExiste() {
        when(jpaRepository.findByCodigoPagamento("nao-existe"))
                .thenReturn(Optional.empty());

        Optional<Venda> result = adapter.buscarPorCodigoPagamento("nao-existe");

        assertThat(result).isEmpty();
    }

    @Test
    void listarAprovadas_deveRetornarListaOrdenada() {
        when(jpaRepository.findByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.APROVADO))
                .thenReturn(List.of(entityBase()));

        List<Venda> result = adapter.listarAprovadas();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMarcaVeiculo()).isEqualTo("Toyota");
    }

    @Test
    void listarAprovadas_deveRetornarListaVazia() {
        when(jpaRepository.findByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.APROVADO))
                .thenReturn(List.of());

        assertThat(adapter.listarAprovadas()).isEmpty();
    }
}
