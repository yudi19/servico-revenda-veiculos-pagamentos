package com.revendas.revendas.pagamentos.application.port.out;

import com.revendas.revendas.pagamentos.domain.model.Venda;

import java.util.List;
import java.util.Optional;

public interface VendaRepositoryPort {
    Venda salvar(Venda venda);
    Optional<Venda> buscarPorCodigoPagamento(String codigoPagamento);
    List<Venda> listarAprovadas();
}
