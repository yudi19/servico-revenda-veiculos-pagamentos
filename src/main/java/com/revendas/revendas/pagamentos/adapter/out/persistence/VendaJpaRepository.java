package com.revendas.revendas.pagamentos.adapter.out.persistence;

import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendaJpaRepository extends JpaRepository<VendaJpaEntity, Long> {
    Optional<VendaJpaEntity> findByCodigoPagamento(String codigoPagamento);
    List<VendaJpaEntity> findByStatusPagamentoOrderByPrecoVeiculoAsc(StatusPagamento status);
}
