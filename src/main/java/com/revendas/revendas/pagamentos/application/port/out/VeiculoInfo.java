package com.revendas.revendas.pagamentos.application.port.out;

import java.math.BigDecimal;

public record VeiculoInfo(
        Long id,
        String marca,
        String modelo,
        Integer ano,
        BigDecimal preco,
        String status
) {}
