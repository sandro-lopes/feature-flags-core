package com.codingbetter.featureflags.domain.model;

/**
 * Enumeração que representa os tipos de valores suportados por feature flags
 * conforme a especificação OpenFeature.
 * 
 * <p>OpenFeature suporta quatro tipos primitivos de valores para feature flags:
 * <ul>
 *   <li>BOOLEAN - valores verdadeiro/falso</li>
 *   <li>STRING - valores de texto</li>
 *   <li>NUMBER - valores numéricos (inteiros ou decimais)</li>
 *   <li>OBJECT - valores estruturados (JSON objects)</li>
 * </ul>
 * 
 * @see <a href="https://openfeature.dev/specification/types/#flag-value-types">OpenFeature Specification - Flag Value Types</a>
 */
public enum ValueType {
    
    /**
     * Tipo booleano - representa valores verdadeiro ou falso.
     * Usado para feature flags simples de ativar/desativar.
     */
    BOOLEAN,
    
    /**
     * Tipo string - representa valores de texto.
     * Usado para feature flags que retornam configurações como nomes, mensagens, etc.
     */
    STRING,
    
    /**
     * Tipo numérico - representa valores numéricos (inteiros ou decimais).
     * Usado para feature flags que retornam números como limites, percentuais, etc.
     */
    NUMBER,
    
    /**
     * Tipo objeto - representa valores estruturados (JSON objects).
     * Usado para feature flags que retornam configurações complexas.
     */
    OBJECT
}

