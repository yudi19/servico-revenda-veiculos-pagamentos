package com.revendas.revendas.pagamentos.adapter.out.http;

import java.math.BigDecimal;

public record VeiculoClientResponse(
        Long id,
        String marca,
        String modelo,
        Integer ano,
        String cor,
        BigDecimal preco,
        String placa,
        String status
) {}
