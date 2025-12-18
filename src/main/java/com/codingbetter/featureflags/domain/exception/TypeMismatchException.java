package com.codingbetter.featureflags.domain.exception;

import com.codingbetter.featureflags.domain.model.ValueType;

/**
 * Exceção lançada quando há incompatibilidade de tipos na avaliação de uma flag.
 * 
 * <p>Esta exceção é lançada quando o tipo de valor solicitado não corresponde
 * ao tipo real da flag. Por exemplo:
 * <ul>
 *   <li>Tentar obter um valor booleano de uma flag do tipo string</li>
 *   <li>Tentar obter um valor numérico de uma flag do tipo booleano</li>
 *   <li>Tentar obter um objeto de um tipo diferente do esperado</li>
 * </ul>
 */
public class TypeMismatchException extends FeatureFlagException {
    
    private final String flagKey;
    private final ValueType expectedType;
    private final ValueType actualType;
    
    /**
     * Constrói uma nova exceção de incompatibilidade de tipos.
     * 
     * @param flagKey A chave da flag
     * @param expectedType O tipo esperado
     * @param actualType O tipo real da flag
     */
    public TypeMismatchException(String flagKey, ValueType expectedType, ValueType actualType) {
        super(String.format(
            "Incompatibilidade de tipos para a flag '%s': esperado %s, mas encontrado %s",
            flagKey, expectedType, actualType
        ));
        this.flagKey = flagKey;
        this.expectedType = expectedType;
        this.actualType = actualType;
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
     * Retorna o tipo esperado.
     * 
     * @return O tipo esperado
     */
    public ValueType getExpectedType() {
        return expectedType;
    }
    
    /**
     * Retorna o tipo real da flag.
     * 
     * @return O tipo real
     */
    public ValueType getActualType() {
        return actualType;
    }
}

