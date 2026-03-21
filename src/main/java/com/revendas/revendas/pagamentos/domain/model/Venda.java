package com.revendas.revendas.pagamentos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Venda {
    private Long id;
    private Long veiculoId;
    private String marcaVeiculo;
    private String modeloVeiculo;
    private Integer anoVeiculo;
    private BigDecimal precoVeiculo;
    private String cpfComprador;
    private LocalDate dataVenda;
    private String codigoPagamento;
    private StatusPagamento statusPagamento;
}
