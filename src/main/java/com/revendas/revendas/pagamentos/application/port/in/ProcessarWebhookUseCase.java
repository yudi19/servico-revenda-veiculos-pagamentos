package com.revendas.revendas.pagamentos.application.port.in;

import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;
import com.revendas.revendas.pagamentos.domain.model.Venda;

public interface ProcessarWebhookUseCase {
    Venda processar(String codigoPagamento, StatusPagamento status);
}
