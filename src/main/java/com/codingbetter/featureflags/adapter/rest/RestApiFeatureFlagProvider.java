package com.codingbetter.featureflags.adapter.rest;

import com.codingbetter.featureflags.domain.model.ErrorCode;
import com.codingbetter.featureflags.domain.model.EvaluationContext;
import com.codingbetter.featureflags.domain.model.FlagEvaluation;
import com.codingbetter.featureflags.domain.model.FlagMetadata;
import com.codingbetter.featureflags.domain.model.ValueType;
import com.codingbetter.featureflags.adapter.rest.dto.FuncionalidadeResponse;
import com.codingbetter.featureflags.port.outbound.FeatureFlagProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Implementação de {@link FeatureFlagProvider} que integra com a
 * API externa de Feature Toggles (consulta de funcionalidades) via REST.
 *
 * <p>Este adapter pertence à camada de adapter (Outbound Adapter)
 * na Arquitetura Hexagonal, conectando o domínio a um serviço REST
 * específico, sem acoplar o domínio a detalhes de infraestrutura.</p>
 */
public class RestApiFeatureFlagProvider implements FeatureFlagProvider {

    private final RestApiFeatureToggleClient client;
    private final ObjectMapper objectMapper;

    public RestApiFeatureFlagProvider(RestApiFeatureToggleClient client) {
        this(client, new ObjectMapper());
    }

    public RestApiFeatureFlagProvider(RestApiFeatureToggleClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    @Override
    public FlagEvaluation<Boolean> getBooleanValue(String flagKey, Boolean defaultValue, EvaluationContext context) {
        try {
            FuncionalidadeResponse response = client.getFuncionalidadePorChave(flagKey, context);
            if (response == null) {
                return errorEvaluation(defaultValue, ErrorCode.FLAG_NOT_FOUND, "Flag não encontrada: " + flagKey);
            }

            if (hasRemoteError(response)) {
                return remoteErrorEvaluation(defaultValue, response);
            }

            Boolean parsed;
            try {
                parsed = Boolean.parseBoolean(response.getValue());
            } catch (RuntimeException ex) {
                return errorEvaluation(defaultValue, ErrorCode.TYPE_MISMATCH,
                        "Não foi possível converter o valor da flag para boolean: " + response.getValue());
            }

            return successEvaluation(parsed, response);
        } catch (RestClientException ex) {
            return errorEvaluation(defaultValue, ErrorCode.NETWORK_ERROR, ex.getMessage());
        }
    }

    @Override
    public FlagEvaluation<String> getStringValue(String flagKey, String defaultValue, EvaluationContext context) {
        try {
            FuncionalidadeResponse response = client.getFuncionalidadePorChave(flagKey, context);
            if (response == null) {
                return errorEvaluation(defaultValue, ErrorCode.FLAG_NOT_FOUND, "Flag não encontrada: " + flagKey);
            }
            if (hasRemoteError(response)) {
                return remoteErrorEvaluation(defaultValue, response);
            }
            return successEvaluation(response.getValue(), response);
        } catch (RestClientException ex) {
            return errorEvaluation(defaultValue, ErrorCode.NETWORK_ERROR, ex.getMessage());
        }
    }

    @Override
    public FlagEvaluation<Double> getNumberValue(String flagKey, Double defaultValue, EvaluationContext context) {
        try {
            FuncionalidadeResponse response = client.getFuncionalidadePorChave(flagKey, context);
            if (response == null) {
                return errorEvaluation(defaultValue, ErrorCode.FLAG_NOT_FOUND, "Flag não encontrada: " + flagKey);
            }
            if (hasRemoteError(response)) {
                return remoteErrorEvaluation(defaultValue, response);
            }

            Double parsed;
            try {
                parsed = Double.parseDouble(response.getValue());
            } catch (RuntimeException ex) {
                return errorEvaluation(defaultValue, ErrorCode.TYPE_MISMATCH,
                        "Não foi possível converter o valor da flag para número: " + response.getValue());
            }

            return successEvaluation(parsed, response);
        } catch (RestClientException ex) {
            return errorEvaluation(defaultValue, ErrorCode.NETWORK_ERROR, ex.getMessage());
        }
    }

    @Override
    public <T> FlagEvaluation<T> getObjectValue(String flagKey, T defaultValue, EvaluationContext context) {
        try {
            FuncionalidadeResponse response = client.getFuncionalidadePorChave(flagKey, context);
            if (response == null) {
                return errorEvaluation(defaultValue, ErrorCode.FLAG_NOT_FOUND, "Flag não encontrada: " + flagKey);
            }
            if (hasRemoteError(response)) {
                return remoteErrorEvaluation(defaultValue, response);
            }

            if (defaultValue == null) {
                // Sem tipo alvo conhecido, devolve o próprio valor String como fallback
                @SuppressWarnings("unchecked")
                T valueAsString = (T) response.getValue();
                return successEvaluation(valueAsString, response);
            }

            try {
                @SuppressWarnings("unchecked")
                Class<T> targetType = (Class<T>) defaultValue.getClass();
                T parsed = objectMapper.readValue(response.getValue(), targetType);
                return successEvaluation(parsed, response);
            } catch (JsonProcessingException ex) {
                return errorEvaluation(defaultValue, ErrorCode.PARSE_ERROR,
                        "Erro ao desserializar valor da flag para tipo " + defaultValue.getClass().getSimpleName());
            }
        } catch (RestClientException ex) {
            return errorEvaluation(defaultValue, ErrorCode.NETWORK_ERROR, ex.getMessage());
        }
    }

    @Override
    public String getName() {
        return "RestApiFeatureFlagProvider";
    }

    @Override
    public ValueType getFlagValueType(String flagKey) {
        try {
            FuncionalidadeResponse response = client.getFuncionalidadePorChave(flagKey, null);
            if (response == null || hasRemoteError(response) || response.getValue() == null) {
                return null;
            }

            String raw = response.getValue();
            if (raw.equalsIgnoreCase("true") || raw.equalsIgnoreCase("false")) {
                return ValueType.BOOLEAN;
            }

            try {
                Double.parseDouble(raw);
                return ValueType.NUMBER;
            } catch (NumberFormatException ignore) {
                // segue para STRING
            }

            // Tenta verificar se parece JSON de objeto
            String trimmed = raw.trim();
            if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
                return ValueType.OBJECT;
            }

            return ValueType.STRING;
        } catch (RestClientException ex) {
            // Em caso de erro de rede ou outro, não conseguimos inferir o tipo
            return null;
        }
    }

    @Override
    public boolean isReady() {
        // Como o provedor é baseado em chamadas REST síncronas,
        // consideramos "pronto" se o cliente foi construído.
        // Falhas de rede serão tratadas a cada chamada.
        return true;
    }

    private boolean hasRemoteError(FuncionalidadeResponse response) {
        return response.getErrorCode() != null && !response.getErrorCode().isBlank();
    }

    private ErrorCode mapRemoteErrorCode(String remoteCode) {
        if (remoteCode == null) {
            return ErrorCode.GENERAL;
        }
        String code = remoteCode.toUpperCase(Locale.ROOT);
        try {
            // Tenta mapear diretamente se o nome coincidir
            return ErrorCode.valueOf(code);
        } catch (IllegalArgumentException ex) {
            // Mapeia códigos conhecidos adicionais ou usa GENERAL como fallback
            if ("FLAG_NOT_FOUND".equals(code)) {
                return ErrorCode.FLAG_NOT_FOUND;
            }
            if ("PROVIDER_NOT_READY".equals(code)) {
                return ErrorCode.PROVIDER_NOT_READY;
            }
            if ("TYPE_MISMATCH".equals(code)) {
                return ErrorCode.TYPE_MISMATCH;
            }
            if ("PARSE_ERROR".equals(code)) {
                return ErrorCode.PARSE_ERROR;
            }
            return ErrorCode.GENERAL;
        }
    }

    private <T> FlagEvaluation<T> successEvaluation(T value, FuncionalidadeResponse response) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("flagkey", response.getFlagkey());
        if (response.getReason() != null) {
            metadata.put("remoteReason", response.getReason());
        }
        if (response.getVariant() != null) {
            metadata.put("remoteVariant", response.getVariant());
        }
        FlagMetadata flagMetadata = new FlagMetadata(metadata);
        return new FlagEvaluation<>(value, response.getVariant(), response.getReason(), flagMetadata);
    }

    private <T> FlagEvaluation<T> remoteErrorEvaluation(T defaultValue, FuncionalidadeResponse response) {
        ErrorCode mapped = mapRemoteErrorCode(response.getErrorCode());
        String message = response.getErrorMessage() != null
                ? response.getErrorMessage()
                : "Erro ao avaliar flag na API remota (código: " + response.getErrorCode() + ")";
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("flagkey", response.getFlagkey());
        FlagMetadata flagMetadata = new FlagMetadata(metadata);
        return new FlagEvaluation<>(defaultValue, mapped, message, flagMetadata);
    }

    private <T> FlagEvaluation<T> errorEvaluation(T defaultValue, ErrorCode errorCode, String message) {
        FlagMetadata metadata = new FlagMetadata();
        return new FlagEvaluation<>(defaultValue, errorCode, message, metadata);
    }
}

