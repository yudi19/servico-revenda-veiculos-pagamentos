package com.revendas.revendas.pagamentos.domain.exception;

public class VendaNaoEncontradaException extends RuntimeException {
    public VendaNaoEncontradaException(String codigoPagamento) {
        super("Venda não encontrada para o código de pagamento: " + codigoPagamento);
    }
}
