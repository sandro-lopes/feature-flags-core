package com.codingbetter.featureflags.adapter.rest;

import com.codingbetter.featureflags.domain.model.EvaluationContext;
import com.codingbetter.featureflags.domain.model.banking.BankingAttributes;
import com.codingbetter.featureflags.adapter.rest.dto.FuncionalidadeResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
    private static final String BEARER_PREFIX = "Bearer ";

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final TokenProvider tokenProvider;

    /**
     * Cria um novo cliente REST.
     *
     * @param restTemplate RestTemplate configurado pelo Spring Boot
     * @param baseUrl      URL base da API de feature toggles (ex: https://api.interna/feature-toggle)
     * @param tokenProvider Provider opcional para obter token quando não há contexto HTTP (pode ser nulo)
     */
    public RestApiFeatureToggleClient(RestTemplate restTemplate,
                                       String baseUrl,
                                       TokenProvider tokenProvider) {
        this.restTemplate = restTemplate;
        this.baseUrl = removeTrailingSlash(baseUrl);
        this.tokenProvider = tokenProvider;
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

    /**
     * Constrói os headers HTTP a partir do contexto de avaliação.
     *
     * <p>Estratégia para token de autenticação (em ordem de prioridade):
     * <ol>
     *   <li>Header "Authorization" do contexto HTTP da requisição atual (HttpServletRequest)</li>
     *   <li>Token do TokenProvider configurado (fallback para chamadas fora de contexto HTTP)</li>
     *   <li>Se nenhum token for encontrado, o header Authorization não é adicionado</li>
     * </ol>
     *
     * <p>O token é obtido diretamente do contexto HTTP da requisição, sem passar pelo
     * EvaluationContext, pois autenticação é uma preocupação de infraestrutura, não do domínio.
     *
     * <p>Se a API exigir autenticação e nenhum token for fornecido, a requisição falhará
     * com HTTP 401, que será tratado como erro de rede pelo provider.
     *
     * @param context contexto de avaliação (pode ser nulo)
     * @return headers HTTP configurados
     */
    private HttpHeaders buildHeadersFromContext(EvaluationContext context) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(MediaType.parseMediaTypes(MediaType.APPLICATION_JSON_VALUE));

        // Estratégia para token: contexto HTTP da requisição > TokenProvider
        String authorizationHeader = extractAuthorizationFromHttpRequest();
        if (authorizationHeader != null && !authorizationHeader.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);
        } else if (tokenProvider != null) {
            // Fallback para TokenProvider (útil para jobs, schedulers, etc.)
            String token = tokenProvider.getToken();
            if (token != null && !token.isBlank()) {
                // Garante que o token tenha o prefixo "Bearer " se necessário
                String formattedToken = token.startsWith(BEARER_PREFIX) 
                    ? token 
                    : BEARER_PREFIX + token;
                headers.set(HttpHeaders.AUTHORIZATION, formattedToken);
            }
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
     * Extrai o header Authorization do contexto HTTP da requisição atual.
     *
     * <p>Este método acessa o HttpServletRequest através do RequestContextHolder do Spring,
     * permitindo que o adapter obtenha o token de autenticação sem acoplar o domínio
     * a detalhes de infraestrutura HTTP.
     *
     * @return valor do header Authorization (ex: "Bearer token123") ou null se não encontrado
     */
    private String extractAuthorizationFromHttpRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                if (request != null) {
                    return request.getHeader(HttpHeaders.AUTHORIZATION);
                }
            }
        } catch (Exception e) {
            // Se não houver contexto de requisição (ex: chamada fora de thread HTTP),
            // retorna null e usa o fallback do TokenProvider (se configurado)
        }
        return null;
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

