package com.revendas.revendas.pagamentos.domain.exception;

public class VeiculoNaoDisponivelException extends RuntimeException {
    public VeiculoNaoDisponivelException(Long veiculoId) {
        super("Veículo com id " + veiculoId + " não está disponível para venda.");
    }
}
