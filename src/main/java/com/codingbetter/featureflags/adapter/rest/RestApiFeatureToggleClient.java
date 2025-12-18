package com.codingbetter.featureflags.adapter.rest;

import com.codingbetter.featureflags.domain.model.EvaluationContext;
import com.codingbetter.featureflags.domain.model.banking.BankingAttributes;
import com.codingbetter.featureflags.adapter.rest.dto.FuncionalidadeResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

/**
 * Cliente REST responsável por integrar com a API externa de Feature Toggles
 * (consulta de funcionalidades) definida pelo contrato OpenAPI.
 *
 * <p>Esta classe pertence à camada de adapter (Outbound Adapter) e
 * converte chamadas de alto nível em requisições HTTP específicas da API.</p>
 */
public class RestApiFeatureToggleClient {

    private static final String HEADER_CANAL = "x-type-value-canal";

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String staticBearerToken;

    /**
     * Cria um novo cliente REST.
     *
     * @param restTemplate     RestTemplate configurado pelo Spring Boot
     * @param baseUrl          URL base da API de feature toggles (ex: https://api.interna/feature-toggle)
     * @param staticBearerToken Token Bearer estático opcional (pode ser nulo se a autenticação for tratada externamente)
     */
    public RestApiFeatureToggleClient(RestTemplate restTemplate,
                                       String baseUrl,
                                       String staticBearerToken) {
        this.restTemplate = restTemplate;
        this.baseUrl = removeTrailingSlash(baseUrl);
        this.staticBearerToken = staticBearerToken;
    }

    /**
     * Consulta uma funcionalidade (flag) por chave de jornada, aplicando opcionalmente contexto.
     *
     * <p>Endpoint principal utilizado:
     * <pre>/funcionalidades/jornadas/{chave}/parametros?valorContexto=agencia=100</pre>
     *
     * @param chave   chave da jornada / funcionalidade (mapeada a partir de flagKey)
     * @param context contexto de avaliação (pode ser nulo)
     * @return resposta da funcionalidade, ou null se não encontrada (HTTP 404)
     * @throws org.springframework.web.client.RestClientException em caso de erro de comunicação ou HTTP não tratado explicitamente
     */
    public FuncionalidadeResponse getFuncionalidadePorChave(String chave, EvaluationContext context) {
        String url = baseUrl + "/funcionalidades/jornadas/{chave}/parametros";

        HttpHeaders headers = buildHeadersFromContext(context);
        MultiValueMap<String, String> queryParams = buildQueryParamsFromContext(context);

        URI uri = restTemplate
                .getUriTemplateHandler()
                .expand(url + buildQueryString(queryParams), Map.of("chave", chave));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<FuncionalidadeResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    FuncionalidadeResponse.class
            );
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            // 404 - recurso não encontrado
            return null;
        }
    }

    private HttpHeaders buildHeadersFromContext(EvaluationContext context) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(MediaType.parseMediaTypes(MediaType.APPLICATION_JSON_VALUE));

        if (staticBearerToken != null && !staticBearerToken.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + staticBearerToken);
        }

        if (context != null) {
            String canal = context.getStringAttribute(BankingAttributes.CANAL);
            if (canal != null) {
                headers.set(HEADER_CANAL, canal);
            }
        }
        return headers;
    }

    /**
     * Constrói o parâmetro valorContexto a partir do EvaluationContext.
     *
     * <p>Estratégia:
     * <ul>
     *     <li>Se existir um atributo explícito "valorContexto", ele é usado diretamente.</li>
     *     <li>Senão, tenta usar atributos específicos do domínio bancário (ex: agencia, canal).</li>
     *     <li>Se nenhum atributo conhecido estiver presente, não envia valorContexto.</li>
     * </ul>
     */
    private MultiValueMap<String, String> buildQueryParamsFromContext(EvaluationContext context) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (context == null) {
            return params;
        }

        Map<String, Object> attributes = context.getAttributes();

        // 1) Atributo explícito
        Object valorContextoAttr = attributes.get("valorContexto");
        if (valorContextoAttr instanceof String valorContextoStr && !valorContextoStr.isBlank()) {
            params.add("valorContexto", valorContextoStr);
            return params;
        }

        // 2) Heurística baseada em atributos bancários conhecidos
        Optional<String> primeiroContexto = firstNonNull(
                () -> buildKeyValue("agencia", attributes.get("agencia")),
                () -> buildKeyValue("codigoAgencia", attributes.get(BankingAttributes.CODIGO_AGENCIA)),
                () -> buildKeyValue("canal", attributes.get(BankingAttributes.CANAL)),
                () -> buildKeyValue("segmentoCliente", attributes.get(BankingAttributes.SEGMENTO_CLIENTE))
        );

        primeiroContexto.ifPresent(v -> params.add("valorContexto", v));
        return params;
    }

    @SafeVarargs
    private static <T> Optional<T> firstNonNull(SupplierWithException<T>... suppliers) {
        for (SupplierWithException<T> supplier : suppliers) {
            try {
                T value = supplier.get();
                if (value != null) {
                    return Optional.of(value);
                }
            } catch (RuntimeException ex) {
                // Ignora erros de conversão e segue para o próximo
            }
        }
        return Optional.empty();
    }

    private String buildQueryString(MultiValueMap<String, String> params) {
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("?");
        params.forEach((key, values) -> {
            for (String value : values) {
                if (sb.length() > 1) {
                    sb.append("&");
                }
                sb.append(key).append("=").append(value);
            }
        });
        return sb.toString();
    }

    private static String buildKeyValue(String key, Object value) {
        if (value == null) {
            return null;
        }
        String v = String.valueOf(value);
        if (v.isBlank()) {
            return null;
        }
        return key + "=" + v;
    }

    private static String removeTrailingSlash(String url) {
        if (url == null) {
            return "";
        }
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get();
    }
}

