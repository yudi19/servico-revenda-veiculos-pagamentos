package com.revendas.revendas.pagamentos.adapter.in.web;

import com.revendas.revendas.pagamentos.application.port.in.EfetuarVendaUseCase;
import com.revendas.revendas.pagamentos.application.port.in.ListarVendidasUseCase;
import com.revendas.revendas.pagamentos.application.port.in.ProcessarWebhookUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final EfetuarVendaUseCase efetuarVendaUseCase;
    private final ProcessarWebhookUseCase processarWebhookUseCase;
    private final ListarVendidasUseCase listarVendidasUseCase;

    @PostMapping
    public ResponseEntity<VendaResponseDTO> efetuarVenda(@Valid @RequestBody EfetuarVendaRequestDTO dto) {
        var venda = efetuarVendaUseCase.efetuarVenda(
                dto.veiculoId(),
                dto.cpfComprador(),
                dto.dataVenda()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(VendaWebMapper.toResponse(venda));
    }

    @PostMapping("/webhook")
    public ResponseEntity<VendaResponseDTO> webhook(@Valid @RequestBody WebhookRequestDTO dto) {
        var venda = processarWebhookUseCase.processar(dto.codigoPagamento(), dto.status());
        return ResponseEntity.ok(VendaWebMapper.toResponse(venda));
    }

    @GetMapping("/vendidos")
    public ResponseEntity<List<VendaResponseDTO>> listarVendidos() {
        List<VendaResponseDTO> lista = listarVendidasUseCase.listarVendidas()
                .stream()
                .map(VendaWebMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }
}
