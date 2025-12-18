package com.codingbetter.featureflags.domain.model;

import java.util.Map;

/**
 * Representa metadados adicionais sobre uma feature flag.
 * 
 * <p>Os metadados podem conter informações adicionais sobre a flag,
 * como descrição, tags, ou qualquer outro dado estruturado fornecido
 * pelo provedor de feature flags.
 * 
 * <p>Conforme a especificação OpenFeature, os metadados são opcionais
 * e podem variar entre diferentes provedores.
 */
public class FlagMetadata {
    
    private final Map<String, Object> attributes;
    
    /**
     * Constrói metadados com os atributos fornecidos.
     * 
     * @param attributes Mapa de atributos de metadados
     */
    public FlagMetadata(Map<String, Object> attributes) {
        this.attributes = attributes != null ? Map.copyOf(attributes) : Map.of();
    }
    
    /**
     * Constrói metadados vazios.
     */
    public FlagMetadata() {
        this.attributes = Map.of();
    }
    
    /**
     * Retorna o valor de um atributo de metadado.
     * 
     * @param key A chave do atributo
     * @return O valor do atributo, ou null se não existir
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }
    
    /**
     * Retorna todos os atributos de metadados.
     * 
     * @return Um mapa imutável com todos os atributos
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    /**
     * Verifica se um atributo existe nos metadados.
     * 
     * @param key A chave do atributo
     * @return true se o atributo existir, false caso contrário
     */
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }
}

