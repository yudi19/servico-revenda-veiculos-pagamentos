package com.revendas.revendas.pagamentos.adapter.in.web;

import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VendaResponseDTO(
        Long id,
        Long veiculoId,
        String marcaVeiculo,
        String modeloVeiculo,
        Integer anoVeiculo,
        BigDecimal precoVeiculo,
        String cpfComprador,
        LocalDate dataVenda,
        String codigoPagamento,
        StatusPagamento statusPagamento
) {}
