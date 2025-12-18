package com.codingbetter.featureflags.domain.exception;

import com.codingbetter.featureflags.domain.model.ErrorCode;

/**
 * Exceção lançada quando ocorre um erro durante a avaliação de uma feature flag.
 * 
 * <p>Esta exceção representa erros gerais que podem ocorrer durante a avaliação,
 * como erros de rede, parse, ou outros erros não categorizados.
 */
public class EvaluationException extends FeatureFlagException {
    
    private final String flagKey;
    private final ErrorCode errorCode;
    
    /**
     * Constrói uma nova exceção de avaliação.
     * 
     * @param flagKey A chave da flag que falhou
     * @param errorCode O código do erro
     * @param message A mensagem de erro
     */
    public EvaluationException(String flagKey, ErrorCode errorCode, String message) {
        super(String.format("Erro ao avaliar flag '%s': %s", flagKey, message));
        this.flagKey = flagKey;
        this.errorCode = errorCode;
    }
    
    /**
     * Constrói uma nova exceção com causa.
     * 
     * @param flagKey A chave da flag que falhou
     * @param errorCode O código do erro
     * @param message A mensagem de erro
     * @param cause A causa da exceção
     */
    public EvaluationException(String flagKey, ErrorCode errorCode, String message, Throwable cause) {
        super(String.format("Erro ao avaliar flag '%s': %s", flagKey, message), cause);
        this.flagKey = flagKey;
        this.errorCode = errorCode;
    }
    
    /**
     * Retorna a chave da flag.
     * 
     * @return A chave da flag
     */
    public String getFlagKey() {
        return flagKey;
    }
    
    /**
     * Retorna o código do erro.
     * 
     * @return O código do erro
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

