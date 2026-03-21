package com.revendas.revendas.pagamentos.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class RestTemplateConfigTest {

    @Test
    void restTemplate_deveRetornarInstancia() {
        RestTemplateConfig config = new RestTemplateConfig();
        RestTemplate result = config.restTemplate();
        assertThat(result).isNotNull().isInstanceOf(RestTemplate.class);
    }
}
