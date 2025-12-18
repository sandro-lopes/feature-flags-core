package com.codingbetter.featureflags.domain.model;

import java.util.Map;

/**
 * Representa o contexto de avaliação usado para avaliar feature flags.
 * 
 * <p>O contexto contém informações que podem influenciar a avaliação de uma flag,
 * como identificadores de usuário, atributos customizados, e outras informações
 * relevantes para o targeting e a segmentação.
 * 
 * <p>Conforme a especificação OpenFeature, o contexto é essencial para:
 * <ul>
 *   <li>Targeting - direcionar flags para usuários específicos</li>
 *   <li>Segmentação - agrupar usuários por características</li>
 *   <li>A/B Testing - distribuir variações baseadas em atributos</li>
 * </ul>
 * 
 * <p>O contexto deve ser imutável após a criação para garantir consistência
 * durante a avaliação.
 * 
 * @see <a href="https://openfeature.dev/specification/types/#evaluation-context">OpenFeature Specification - Evaluation Context</a>
 */
public interface EvaluationContext {
    
    /**
     * Retorna a chave de targeting (targeting key).
     * 
     * <p>A targeting key é um identificador único usado para direcionar
     * feature flags para entidades específicas (usuários, sessões, etc.).
     * É o atributo mais importante do contexto para targeting.
     * 
     * <p>Exemplos de targeting keys:
     * <ul>
     *   <li>ID do usuário: "user-12345"</li>
     *   <li>ID da sessão: "session-abc123"</li>
     *   <li>ID da organização: "org-789"</li>
     * </ul>
     * 
     * @return A chave de targeting, ou null se não fornecida
     */
    String getTargetingKey();
    
    /**
     * Retorna o valor de um atributo do contexto.
     * 
     * <p>Os atributos são pares chave-valor que fornecem informações adicionais
     * sobre o contexto de avaliação. Estes atributos podem ser usados em regras
     * de targeting, segmentação e distribuição.
     * 
     * <p>Exemplos de atributos:
     * <ul>
     *   <li>"email" -> "user@example.com"</li>
     *   <li>"country" -> "BR"</li>
     *   <li>"plan" -> "premium"</li>
     *   <li>"age" -> 25</li>
     * </ul>
     * 
     * @param key A chave do atributo
     * @return O valor do atributo, ou null se não existir
     */
    Object getAttribute(String key);
    
    /**
     * Retorna todos os atributos do contexto.
     * 
     * <p>Este método retorna um mapa com todos os atributos do contexto,
     * incluindo a targeting key se disponível.
     * 
     * @return Um mapa imutável com todos os atributos do contexto
     */
    Map<String, Object> getAttributes();
    
    /**
     * Verifica se um atributo existe no contexto.
     * 
     * @param key A chave do atributo
     * @return true se o atributo existir, false caso contrário
     */
    default boolean hasAttribute(String key) {
        return getAttributes().containsKey(key);
    }
    
    /**
     * Retorna o valor de um atributo como String.
     * 
     * @param key A chave do atributo
     * @return O valor como String, ou null se não existir ou não for String
     */
    default String getStringAttribute(String key) {
        Object value = getAttribute(key);
        return value instanceof String ? (String) value : null;
    }
    
    /**
     * Retorna o valor de um atributo como Integer.
     * 
     * @param key A chave do atributo
     * @return O valor como Integer, ou null se não existir ou não for Integer
     */
    default Integer getIntegerAttribute(String key) {
        Object value = getAttribute(key);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }
    
    /**
     * Retorna o valor de um atributo como Boolean.
     * 
     * @param key A chave do atributo
     * @return O valor como Boolean, ou null se não existir ou não for Boolean
     */
    default Boolean getBooleanAttribute(String key) {
        Object value = getAttribute(key);
        return value instanceof Boolean ? (Boolean) value : null;
    }
}

