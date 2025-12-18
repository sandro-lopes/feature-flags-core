package com.codingbetter.featureflags.port.inbound;

import com.codingbetter.featureflags.domain.model.EvaluationContext;
import com.codingbetter.featureflags.domain.model.FlagEvaluation;
import com.codingbetter.featureflags.port.outbound.FeatureFlagProvider;

/**
 * Interface principal para avaliação de feature flags (Inbound Port).
 * 
 * <p>O cliente é a interface de alto nível que os desenvolvedores usam
 * para avaliar feature flags em suas aplicações. Ele abstrai a complexidade
 * do provedor e fornece uma API simples e consistente.
 * 
 * <p>Conforme a especificação OpenFeature, o cliente:
 * <ul>
 *   <li>Fornece uma API unificada para avaliação de flags</li>
 *   <li>Gerencia o contexto de avaliação</li>
 *   <li>Executa hooks (se configurados)</li>
 *   <li>Delega a avaliação real para o provedor</li>
 * </ul>
 * 
 * <p>Esta interface foi projetada para ser fácil de usar, com métodos
 * sobrecarregados que permitem avaliação com ou sem contexto explícito.
 * 
 * <p>Exemplo de uso:
 * <pre>{@code
 * FeatureFlagClient client = // obter instância do cliente
 * 
 * // Avaliação simples com valor padrão
 * boolean enabled = client.getBooleanValue("new-feature", false);
 * 
 * // Avaliação com contexto
 * EvaluationContext context = // criar contexto
 * String theme = client.getStringValue("theme", "default", context);
 * }</pre>
 * 
 * @see <a href="https://openfeature.dev/specification/sections/clients">OpenFeature Specification - Clients</a>
 */
public interface FeatureFlagClient {
    
    /**
     * Avalia uma feature flag do tipo booleano.
     * 
     * <p>Este é o método mais comum para verificar se uma feature está habilitada.
     * 
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @return O valor booleano da flag ou o valor padrão
     */
    boolean getBooleanValue(String flagKey, boolean defaultValue);
    
    /**
     * Avalia uma feature flag do tipo booleano com contexto.
     * 
     * <p>Use este método quando precisar fornecer contexto adicional para
     * a avaliação, como informações do usuário para targeting.
     * 
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @param context O contexto de avaliação (pode ser null)
     * @return O valor booleano da flag ou o valor padrão
     */
    boolean getBooleanValue(String flagKey, boolean defaultValue, EvaluationContext context);
    
    /**
     * Avalia uma feature flag do tipo booleano e retorna detalhes completos.
     * 
     * <p>Use este método quando precisar de informações adicionais sobre
     * a avaliação, como a razão do valor retornado ou metadados.
     * 
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @param context O contexto de avaliação (pode ser null)
     * @return Uma FlagEvaluation contendo o valor e detalhes da avaliação
     */
    FlagEvaluation<Boolean> getBooleanEvaluation(String flagKey, boolean defaultValue, EvaluationContext context);
    
    /**
     * Avalia uma feature flag do tipo string.
     * 
     * <p>Use este método para obter valores de configuração como strings.
     * 
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @return O valor string da flag ou o valor padrão
     */
    String getStringValue(String flagKey, String defaultValue);
    
    /**
     * Avalia uma feature flag do tipo string com contexto.
     * 
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @param context O contexto de avaliação (pode ser null)
     * @return O valor string da flag ou o valor padrão
     */
    String getStringValue(String flagKey, String defaultValue, EvaluationContext context);
    
    /**
     * Avalia uma feature flag do tipo string e retorna detalhes completos.
     * 
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @param context O contexto de avaliação (pode ser null)
     * @return Uma FlagEvaluation contendo o valor e detalhes da avaliação
     */
    FlagEvaluation<String> getStringEvaluation(String flagKey, String defaultValue, EvaluationContext context);
    
    /**
     * Avalia uma feature flag do tipo numérico.
     * 
     * <p>Use este método para obter valores numéricos como limites, percentuais, etc.
     * 
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @return O valor numérico da flag ou o valor padrão
     */
    double getNumberValue(String flagKey, double defaultValue);
    
    /**
     * Avalia uma feature flag do tipo numérico com contexto.
     * 
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @param context O contexto de avaliação (pode ser null)
     * @return O valor numérico da flag ou o valor padrão
     */
    double getNumberValue(String flagKey, double defaultValue, EvaluationContext context);
    
    /**
     * Avalia uma feature flag do tipo numérico e retorna detalhes completos.
     * 
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @param context O contexto de avaliação (pode ser null)
     * @return Uma FlagEvaluation contendo o valor e detalhes da avaliação
     */
    FlagEvaluation<Double> getNumberEvaluation(String flagKey, double defaultValue, EvaluationContext context);
    
    /**
     * Avalia uma feature flag do tipo objeto.
     * 
     * <p>Use este método para obter valores estruturados (JSON objects).
     * 
     * @param <T> O tipo do objeto retornado
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @return O valor do objeto da flag ou o valor padrão
     */
    <T> T getObjectValue(String flagKey, T defaultValue);
    
    /**
     * Avalia uma feature flag do tipo objeto com contexto.
     * 
     * @param <T> O tipo do objeto retornado
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @param context O contexto de avaliação (pode ser null)
     * @return O valor do objeto da flag ou o valor padrão
     */
    <T> T getObjectValue(String flagKey, T defaultValue, EvaluationContext context);
    
    /**
     * Avalia uma feature flag do tipo objeto e retorna detalhes completos.
     * 
     * @param <T> O tipo do objeto retornado
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @param context O contexto de avaliação (pode ser null)
     * @return Uma FlagEvaluation contendo o valor e detalhes da avaliação
     */
    <T> FlagEvaluation<T> getObjectEvaluation(String flagKey, T defaultValue, EvaluationContext context);
    
    /**
     * Retorna o nome do cliente.
     * 
     * <p>Útil para identificação e logging.
     * 
     * @return O nome do cliente
     */
    String getName();
    
    /**
     * Retorna o provedor associado a este cliente.
     * 
     * @return O provedor de feature flags
     */
    FeatureFlagProvider getProvider();
}

