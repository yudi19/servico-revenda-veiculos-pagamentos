package com.revendas.revendas.pagamentos.adapter.in.web;

import com.revendas.revendas.pagamentos.domain.model.Venda;

public class VendaWebMapper {

    private VendaWebMapper() {}

    public static VendaResponseDTO toResponse(Venda venda) {
        return new VendaResponseDTO(
                venda.getId(),
                venda.getVeiculoId(),
                venda.getMarcaVeiculo(),
                venda.getModeloVeiculo(),
                venda.getAnoVeiculo(),
                venda.getPrecoVeiculo(),
                venda.getCpfComprador(),
                venda.getDataVenda(),
                venda.getCodigoPagamento(),
                venda.getStatusPagamento()
        );
    }
}
