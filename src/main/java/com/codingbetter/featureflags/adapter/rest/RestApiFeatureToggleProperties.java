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
 *     static-bearer-token: ${FEATURE_TOGGLE_TOKEN}
 * </pre>
 */
@ConfigurationProperties(prefix = "featureflags.rest")
public class RestApiFeatureToggleProperties {

    /**
     * URL base da API de Feature Toggles.
     * Exemplo: {@code https://meu-host-interno/feature-toggle}
     */
    private String baseUrl;

    /**
     * Token Bearer estático opcional.
     * Em cenários mais avançados, recomenda-se integrar com OAuth2 ou
     * outro mecanismo de autenticação gerenciado externamente.
     */
    private String staticBearerToken;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getStaticBearerToken() {
        return staticBearerToken;
    }

    public void setStaticBearerToken(String staticBearerToken) {
        this.staticBearerToken = staticBearerToken;
    }
}

