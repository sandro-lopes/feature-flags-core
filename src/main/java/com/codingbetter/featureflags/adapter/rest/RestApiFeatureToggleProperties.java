package com.codingbetter.featureflags.adapter.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriedades de configuração para o adapter REST da API de Feature Toggles.
 *
 * <p>Exemplo de configuração em <code>application.yml</code>:</p>
 *
 * <pre>
 * featureflags:
 *   rest:
 *     base-url: https://meu-host-interno/feature-toggle
 * </pre>
 *
 * <p>Para autenticação, implemente {@link TokenProvider} como bean Spring para
 * fornecer tokens quando não houver contexto HTTP disponível.
 */
@ConfigurationProperties(prefix = "featureflags.rest")
public class RestApiFeatureToggleProperties {

    /**
     * URL base da API de Feature Toggles.
     * Exemplo: {@code https://meu-host-interno/feature-toggle}
     */
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

