package com.revendas.revendas.pagamentos.application.port.in;

import com.revendas.revendas.pagamentos.domain.model.Venda;

import java.util.List;

public interface ListarVendidasUseCase {
    List<Venda> listarVendidas();
}
