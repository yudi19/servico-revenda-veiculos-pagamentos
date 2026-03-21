package com.revendas.revendas.pagamentos.application.service;

import com.revendas.revendas.pagamentos.application.port.in.EfetuarVendaUseCase;
import com.revendas.revendas.pagamentos.application.port.in.ListarVendidasUseCase;
import com.revendas.revendas.pagamentos.application.port.in.ProcessarWebhookUseCase;
import com.revendas.revendas.pagamentos.application.port.out.VeiculoInfo;
import com.revendas.revendas.pagamentos.application.port.out.VeiculoServicePort;
import com.revendas.revendas.pagamentos.application.port.out.VendaRepositoryPort;
import com.revendas.revendas.pagamentos.domain.exception.VeiculoNaoDisponivelException;
import com.revendas.revendas.pagamentos.domain.exception.VendaNaoEncontradaException;
import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;
import com.revendas.revendas.pagamentos.domain.model.Venda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendaService implements EfetuarVendaUseCase, ProcessarWebhookUseCase, ListarVendidasUseCase {

    private final VendaRepositoryPort vendaRepositoryPort;
    private final VeiculoServicePort veiculoServicePort;

    @Override
    public Venda efetuarVenda(Long veiculoId, String cpfComprador, LocalDate dataVenda) {
        VeiculoInfo veiculo = veiculoServicePort.buscarVeiculo(veiculoId);

        if (!"DISPONIVEL".equals(veiculo.status())) {
            throw new VeiculoNaoDisponivelException(veiculoId);
        }

        Venda venda = Venda.builder()
                .veiculoId(veiculoId)
                .marcaVeiculo(veiculo.marca())
                .modeloVeiculo(veiculo.modelo())
                .anoVeiculo(veiculo.ano())
                .precoVeiculo(veiculo.preco())
                .cpfComprador(cpfComprador)
                .dataVenda(dataVenda)
                .codigoPagamento(UUID.randomUUID().toString())
                .statusPagamento(StatusPagamento.PENDENTE)
                .build();

        Venda vendaSalva = vendaRepositoryPort.salvar(venda);
        veiculoServicePort.atualizarStatusVeiculo(veiculoId, "RESERVADO");

        return vendaSalva;
    }

    @Override
    public Venda processar(String codigoPagamento, StatusPagamento status) {
        Venda venda = vendaRepositoryPort.buscarPorCodigoPagamento(codigoPagamento)
                .orElseThrow(() -> new VendaNaoEncontradaException(codigoPagamento));

        venda.setStatusPagamento(status);
        Venda vendaAtualizada = vendaRepositoryPort.salvar(venda);

        if (status == StatusPagamento.APROVADO) {
            veiculoServicePort.atualizarStatusVeiculo(venda.getVeiculoId(), "VENDIDO");
        } else if (status == StatusPagamento.CANCELADO) {
            veiculoServicePort.atualizarStatusVeiculo(venda.getVeiculoId(), "DISPONIVEL");
        }

        return vendaAtualizada;
    }

    @Override
    public List<Venda> listarVendidas() {
        return vendaRepositoryPort.listarAprovadas();
    }
}
