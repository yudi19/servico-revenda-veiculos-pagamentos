package com.revendas.revendas.pagamentos.adapter.out.persistence;

import com.revendas.revendas.pagamentos.domain.model.Venda;

public class VendaPersistenceMapper {

    private VendaPersistenceMapper() {}

    public static Venda toDomain(VendaJpaEntity entity) {
        return Venda.builder()
                .id(entity.getId())
                .veiculoId(entity.getVeiculoId())
                .marcaVeiculo(entity.getMarcaVeiculo())
                .modeloVeiculo(entity.getModeloVeiculo())
                .anoVeiculo(entity.getAnoVeiculo())
                .precoVeiculo(entity.getPrecoVeiculo())
                .cpfComprador(entity.getCpfComprador())
                .dataVenda(entity.getDataVenda())
                .codigoPagamento(entity.getCodigoPagamento())
                .statusPagamento(entity.getStatusPagamento())
                .build();
    }

    public static VendaJpaEntity toEntity(Venda venda) {
        return VendaJpaEntity.builder()
                .id(venda.getId())
                .veiculoId(venda.getVeiculoId())
                .marcaVeiculo(venda.getMarcaVeiculo())
                .modeloVeiculo(venda.getModeloVeiculo())
                .anoVeiculo(venda.getAnoVeiculo())
                .precoVeiculo(venda.getPrecoVeiculo())
                .cpfComprador(venda.getCpfComprador())
                .dataVenda(venda.getDataVenda())
                .codigoPagamento(venda.getCodigoPagamento())
                .statusPagamento(venda.getStatusPagamento())
                .build();
    }
}
