package com.codingbetter.featureflags.domain.exception;

/**
 * Exceção lançada quando o provedor de feature flags não está pronto.
 * 
 * <p>Esta exceção é lançada quando se tenta avaliar uma flag mas o provedor
 * não está em um estado válido para realizar avaliações. Isso pode ocorrer quando:
 * <ul>
 *   <li>O provedor ainda está inicializando</li>
 *   <li>O provedor perdeu conexão com o sistema de feature flags</li>
 *   <li>O provedor está em estado de erro e precisa ser reinicializado</li>
 * </ul>
 */
public class ProviderNotReadyException extends FeatureFlagException {
    
    private final String providerName;
    
    /**
     * Constrói uma nova exceção para provedor não pronto.
     * 
     * @param providerName O nome do provedor que não está pronto
     */
    public ProviderNotReadyException(String providerName) {
        super("Provedor de feature flags não está pronto: " + providerName);
        this.providerName = providerName;
    }
    
    /**
     * Constrói uma nova exceção com causa.
     * 
     * @param providerName O nome do provedor que não está pronto
     * @param cause A causa da exceção
     */
    public ProviderNotReadyException(String providerName, Throwable cause) {
        super("Provedor de feature flags não está pronto: " + providerName, cause);
        this.providerName = providerName;
    }
    
    /**
     * Retorna o nome do provedor.
     * 
     * @return O nome do provedor
     */
    public String getProviderName() {
        return providerName;
    }
}

