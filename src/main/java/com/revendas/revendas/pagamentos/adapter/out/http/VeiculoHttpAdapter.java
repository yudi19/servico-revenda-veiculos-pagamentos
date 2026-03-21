package com.revendas.revendas.pagamentos.adapter.out.http;

import com.revendas.revendas.pagamentos.application.port.out.VeiculoInfo;
import com.revendas.revendas.pagamentos.application.port.out.VeiculoServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class VeiculoHttpAdapter implements VeiculoServicePort {

    private final RestTemplate restTemplate;

    @Value("${listagem.service.url}")
    private String listagemServiceUrl;

    @Override
    public VeiculoInfo buscarVeiculo(Long veiculoId) {
        String url = listagemServiceUrl + "/veiculos/" + veiculoId;
        VeiculoClientResponse response = restTemplate.getForObject(url, VeiculoClientResponse.class);
        if (response == null) {
            throw new IllegalStateException("Veículo não encontrado no serviço de listagem: " + veiculoId);
        }
        return new VeiculoInfo(
                response.id(),
                response.marca(),
                response.modelo(),
                response.ano(),
                response.preco(),
                response.status()
        );
    }

    @Override
    public void atualizarStatusVeiculo(Long veiculoId, String novoStatus) {
        String url = listagemServiceUrl + "/veiculos/" + veiculoId;

        VeiculoClientResponse atual = restTemplate.getForObject(url, VeiculoClientResponse.class);
        if (atual == null) {
            throw new IllegalStateException("Veículo não encontrado no serviço de listagem: " + veiculoId);
        }

        VeiculoClientRequest request = new VeiculoClientRequest(
                atual.marca(),
                atual.modelo(),
                atual.ano(),
                atual.cor(),
                atual.preco(),
                atual.placa(),
                novoStatus
        );

        restTemplate.put(url, new HttpEntity<>(request));
    }
}
