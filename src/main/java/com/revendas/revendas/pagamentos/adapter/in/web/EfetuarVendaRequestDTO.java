package com.revendas.revendas.pagamentos.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record EfetuarVendaRequestDTO(
        @NotNull(message = "veiculoId é obrigatório")
        Long veiculoId,

        @NotBlank(message = "cpfComprador é obrigatório")
        @CPF(message = "CPF inválido")
        String cpfComprador,

        @NotNull(message = "dataVenda é obrigatória")
        LocalDate dataVenda
) {}
