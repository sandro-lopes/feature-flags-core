package com.codingbetter.featureflags.domain.exception;

/**
 * Exceção base para todos os erros relacionados a feature flags.
 * 
 * <p>Esta é a classe base para todas as exceções específicas do domínio
 * de feature flags. Ela fornece uma estrutura comum para tratamento de erros.
 */
public class FeatureFlagException extends RuntimeException {
    
    /**
     * Constrói uma nova exceção com a mensagem especificada.
     * 
     * @param message A mensagem de erro
     */
    public FeatureFlagException(String message) {
        super(message);
    }
    
    /**
     * Constrói uma nova exceção com a mensagem e causa especificadas.
     * 
     * @param message A mensagem de erro
     * @param cause A causa da exceção
     */
    public FeatureFlagException(String message, Throwable cause) {
        super(message, cause);
    }
}

