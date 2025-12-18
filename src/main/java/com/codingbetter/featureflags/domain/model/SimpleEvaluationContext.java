package com.codingbetter.featureflags.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementação simples e imutável de EvaluationContext.
 * 
 * <p>Esta classe fornece uma implementação conveniente de EvaluationContext
 * que pode ser usada diretamente pelos clientes da biblioteca. O contexto
 * é imutável após a criação, garantindo consistência durante a avaliação.
 * 
 * <p>Exemplo de uso:
 * <pre>{@code
 * EvaluationContext context = SimpleEvaluationContext.builder()
 *     .targetingKey("user-123")
 *     .attribute("email", "user@example.com")
 *     .attribute("country", "BR")
 *     .build();
 * }</pre>
 */
public class SimpleEvaluationContext implements EvaluationContext {
    
    private final String targetingKey;
    private final Map<String, Object> attributes;
    
    /**
     * Constrói um novo contexto com a chave de targeting e atributos especificados.
     * 
     * @param targetingKey A chave de targeting
     * @param attributes Os atributos do contexto
     */
    public SimpleEvaluationContext(String targetingKey, Map<String, Object> attributes) {
        this.targetingKey = targetingKey;
        this.attributes = attributes != null ? Map.copyOf(attributes) : Map.of();
    }
    
    /**
     * Constrói um novo contexto vazio.
     */
    public SimpleEvaluationContext() {
        this(null, null);
    }
    
    @Override
    public String getTargetingKey() {
        return targetingKey;
    }
    
    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    /**
     * Retorna um builder para criar instâncias de SimpleEvaluationContext.
     * 
     * @return Um novo builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder para criar instâncias de SimpleEvaluationContext.
     */
    public static class Builder {
        private String targetingKey;
        private final Map<String, Object> attributes = new HashMap<>();
        
        /**
         * Define a chave de targeting.
         * 
         * @param targetingKey A chave de targeting
         * @return Este builder para encadeamento
         */
        public Builder targetingKey(String targetingKey) {
            this.targetingKey = targetingKey;
            return this;
        }
        
        /**
         * Adiciona um atributo ao contexto.
         * 
         * @param key A chave do atributo
         * @param value O valor do atributo
         * @return Este builder para encadeamento
         */
        public Builder attribute(String key, Object value) {
            this.attributes.put(key, value);
            return this;
        }
        
        /**
         * Adiciona múltiplos atributos ao contexto.
         * 
         * @param attributes Um mapa com os atributos a adicionar
         * @return Este builder para encadeamento
         */
        public Builder attributes(Map<String, Object> attributes) {
            if (attributes != null) {
                this.attributes.putAll(attributes);
            }
            return this;
        }
        
        /**
         * Constrói uma nova instância de SimpleEvaluationContext.
         * 
         * @return Uma nova instância imutável do contexto
         */
        public SimpleEvaluationContext build() {
            return new SimpleEvaluationContext(targetingKey, attributes);
        }
    }
}

