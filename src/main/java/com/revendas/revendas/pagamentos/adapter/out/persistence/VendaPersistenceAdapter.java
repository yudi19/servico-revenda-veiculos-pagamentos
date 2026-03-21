package com.revendas.revendas.pagamentos.adapter.out.persistence;

import com.revendas.revendas.pagamentos.application.port.out.VendaRepositoryPort;
import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;
import com.revendas.revendas.pagamentos.domain.model.Venda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VendaPersistenceAdapter implements VendaRepositoryPort {

    private final VendaJpaRepository jpaRepository;

    @Override
    public Venda salvar(Venda venda) {
        return VendaPersistenceMapper.toDomain(
                jpaRepository.save(VendaPersistenceMapper.toEntity(venda)));
    }

    @Override
    public Optional<Venda> buscarPorCodigoPagamento(String codigoPagamento) {
        return jpaRepository.findByCodigoPagamento(codigoPagamento)
                .map(VendaPersistenceMapper::toDomain);
    }

    @Override
    public List<Venda> listarAprovadas() {
        return jpaRepository
                .findByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento.APROVADO)
                .stream()
                .map(VendaPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
