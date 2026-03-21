package com.revendas.revendas.pagamentos.application.port.out;

public interface VeiculoServicePort {
    VeiculoInfo buscarVeiculo(Long veiculoId);
    void atualizarStatusVeiculo(Long veiculoId, String novoStatus);
}
