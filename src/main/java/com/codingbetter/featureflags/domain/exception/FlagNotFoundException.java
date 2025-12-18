package com.codingbetter.featureflags.domain.exception;

/**
 * Exceção lançada quando uma feature flag não é encontrada.
 * 
 * <p>Esta exceção é lançada quando o provedor não consegue localizar
 * uma flag com a chave fornecida. Isso pode ocorrer quando:
 * <ul>
 *   <li>A flag não existe no sistema de feature flags</li>
 *   <li>A chave da flag está incorreta</li>
 *   <li>A flag foi removida</li>
 * </ul>
 */
public class FlagNotFoundException extends FeatureFlagException {
    
    private final String flagKey;
    
    /**
     * Constrói uma nova exceção para a flag não encontrada.
     * 
     * @param flagKey A chave da flag que não foi encontrada
     */
    public FlagNotFoundException(String flagKey) {
        super("Feature flag não encontrada: " + flagKey);
        this.flagKey = flagKey;
    }
    
    /**
     * Constrói uma nova exceção com causa.
     * 
     * @param flagKey A chave da flag que não foi encontrada
     * @param cause A causa da exceção
     */
    public FlagNotFoundException(String flagKey, Throwable cause) {
        super("Feature flag não encontrada: " + flagKey, cause);
        this.flagKey = flagKey;
    }
    
    /**
     * Retorna a chave da flag que não foi encontrada.
     * 
     * @return A chave da flag
     */
    public String getFlagKey() {
        return flagKey;
    }
}

