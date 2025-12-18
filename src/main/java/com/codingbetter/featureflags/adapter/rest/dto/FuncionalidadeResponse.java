package com.codingbetter.featureflags.adapter.rest.dto;

/**
 * DTO que representa a resposta da API externa de Feature Toggles
 * para uma única funcionalidade, conforme o contrato OpenAPI.
 *
 * <p>Esta classe é usada apenas na camada de adapter REST
 * e não faz parte do domínio.</p>
 */
public class FuncionalidadeResponse {

    private String flagkey;
    private String value;
    private String reason;
    private String variant;
    private String errorCode;
    private String errorMessage;

    public String getFlagkey() {
        return flagkey;
    }

    public void setFlagkey(String flagkey) {
        this.flagkey = flagkey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

