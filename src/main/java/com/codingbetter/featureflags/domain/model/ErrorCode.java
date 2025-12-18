package com.codingbetter.featureflags.domain.model;

/**
 * Enumeração que representa os códigos de erro padronizados
 * conforme a especificação OpenFeature.
 * 
 * <p>Estes códigos de erro são usados para identificar diferentes tipos
 * de problemas que podem ocorrer durante a avaliação de feature flags.
 * 
 * @see <a href="https://openfeature.dev/specification/types/#error-code">OpenFeature Specification - Error Code</a>
 */
public enum ErrorCode {
    
    /**
     * A flag solicitada não foi encontrada.
     * O provedor não conseguiu localizar a flag com a chave fornecida.
     */
    FLAG_NOT_FOUND,
    
    /**
     * O tipo de valor solicitado não corresponde ao tipo real da flag.
     * Por exemplo, tentar obter um valor booleano de uma flag do tipo string.
     */
    TYPE_MISMATCH,
    
    /**
     * O provedor não está pronto para avaliar flags.
     * Pode ocorrer durante a inicialização ou quando o provedor está em estado inválido.
     */
    PROVIDER_NOT_READY,
    
    /**
     * Ocorreu um erro geral durante a avaliação da flag.
     * Este é um código genérico para erros não categorizados.
     */
    GENERAL,
    
    /**
     * Ocorreu um erro de parse ao processar a resposta do provedor.
     * Pode ocorrer quando a resposta não está no formato esperado.
     */
    PARSE_ERROR,
    
    /**
     * Ocorreu um erro de rede ou comunicação com o provedor.
     * Pode ocorrer quando há problemas de conectividade ou timeout.
     */
    NETWORK_ERROR
}

