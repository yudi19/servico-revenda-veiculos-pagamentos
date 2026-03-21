package com.revendas.revendas.pagamentos.adapter.in.web;

import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WebhookRequestDTO(
        @NotBlank(message = "codigoPagamento é obrigatório")
        String codigoPagamento,

        @NotNull(message = "status é obrigatório")
        StatusPagamento status
) {}
