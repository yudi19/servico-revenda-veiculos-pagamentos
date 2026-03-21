package com.revendas.revendas.pagamentos.application.port.in;

import com.revendas.revendas.pagamentos.domain.model.Venda;

import java.time.LocalDate;

public interface EfetuarVendaUseCase {
    Venda efetuarVenda(Long veiculoId, String cpfComprador, LocalDate dataVenda);
}
