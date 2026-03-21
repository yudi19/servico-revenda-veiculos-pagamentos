package com.revendas.revendas.pagamentos.adapter.out.persistence;

import com.revendas.revendas.pagamentos.domain.model.StatusPagamento;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vendas")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long veiculoId;

    @Column(nullable = false)
    private String marcaVeiculo;

    @Column(nullable = false)
    private String modeloVeiculo;

    @Column(nullable = false)
    private Integer anoVeiculo;

    @Column(nullable = false)
    private BigDecimal precoVeiculo;

    @Column(nullable = false, length = 14)
    private String cpfComprador;

    @Column(nullable = false)
    private LocalDate dataVenda;

    @Column(nullable = false, unique = true)
    private String codigoPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento statusPagamento;
}
