package com.codingbetter.featureflags.adapter.rest;

import com.codingbetter.featureflags.port.outbound.FeatureFlagProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuração Spring Boot para registrar os beans necessários
 * do adapter REST de Feature Toggles.
 *
 * <p>Esta configuração segue o estilo de uma biblioteca Spring Boot,
 * permitindo que a aplicação consuma o {@link FeatureFlagProvider}
 * sem precisar conhecer detalhes de infraestrutura.</p>
 */
@Configuration
@EnableConfigurationProperties(RestApiFeatureToggleProperties.class)
public class RestApiFeatureToggleConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "featureFlagsRestTemplate")
    public RestTemplate featureFlagsRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean(RestApiFeatureToggleClient.class)
    public RestApiFeatureToggleClient restApiFeatureToggleClient(
            RestTemplate featureFlagsRestTemplate,
            RestApiFeatureToggleProperties properties
    ) {
        return new RestApiFeatureToggleClient(
                featureFlagsRestTemplate,
                properties.getBaseUrl(),
                properties.getStaticBearerToken()
        );
    }

    @Bean
    @ConditionalOnMissingBean(FeatureFlagProvider.class)
    public FeatureFlagProvider restApiFeatureFlagProvider(RestApiFeatureToggleClient client) {
        return new RestApiFeatureFlagProvider(client);
    }
}

