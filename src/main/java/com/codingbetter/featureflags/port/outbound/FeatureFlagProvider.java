package com.codingbetter.featureflags.port.outbound;

import com.codingbetter.featureflags.domain.model.EvaluationContext;
import com.codingbetter.featureflags.domain.model.FlagEvaluation;
import com.codingbetter.featureflags.domain.model.ValueType;

/**
 * Interface que define o contrato para provedores de feature flags (Outbound Port).
 * 
 * <p>Um provedor é responsável por avaliar feature flags e retornar seus valores.
 * Diferentes implementações podem se conectar a diferentes sistemas de feature flags
 * (LaunchDarkly, Split.io, Flagsmith, APIs REST customizadas, etc.).
 * 
 * <p>Conforme a especificação OpenFeature, o provedor é a camada de abstração
 * que permite que o cliente seja independente do sistema de feature flags específico.
 * 
 * <p>Implementações desta interface devem:
 * <ul>
 *   <li>Conectar-se ao sistema de feature flags (API REST, SDK, etc.)</li>
 *   <li>Avaliar flags baseado no contexto fornecido</li>
 *   <li>Retornar resultados padronizados usando FlagEvaluation</li>
 *   <li>Tratar erros e retornar códigos de erro apropriados</li>
 * </ul>
 * 
 * @see <a href="https://openfeature.dev/specification/sections/providers">OpenFeature Specification - Providers</a>
 */
public interface FeatureFlagProvider {
    
    /**
     * Avalia uma feature flag do tipo booleano.
     * 
     * <p>Este método deve buscar a flag com a chave fornecida e retornar
     * seu valor booleano. Se a flag não for encontrada ou houver erro,
     * deve retornar o valor padrão fornecido.
     * 
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @param context O contexto de avaliação (pode ser null)
     * @return Uma FlagEvaluation contendo o valor booleano ou o valor padrão
     */
    FlagEvaluation<Boolean> getBooleanValue(String flagKey, Boolean defaultValue, EvaluationContext context);
    
    /**
     * Avalia uma feature flag do tipo string.
     * 
     * <p>Este método deve buscar a flag com a chave fornecida e retornar
     * seu valor string. Se a flag não for encontrada ou houver erro,
     * deve retornar o valor padrão fornecido.
     * 
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @param context O contexto de avaliação (pode ser null)
     * @return Uma FlagEvaluation contendo o valor string ou o valor padrão
     */
    FlagEvaluation<String> getStringValue(String flagKey, String defaultValue, EvaluationContext context);
    
    /**
     * Avalia uma feature flag do tipo numérico.
     * 
     * <p>Este método deve buscar a flag com a chave fornecida e retornar
     * seu valor numérico (double). Se a flag não for encontrada ou houver erro,
     * deve retornar o valor padrão fornecido.
     * 
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @param context O contexto de avaliação (pode ser null)
     * @return Uma FlagEvaluation contendo o valor numérico ou o valor padrão
     */
    FlagEvaluation<Double> getNumberValue(String flagKey, Double defaultValue, EvaluationContext context);
    
    /**
     * Avalia uma feature flag do tipo objeto.
     * 
     * <p>Este método deve buscar a flag com a chave fornecida e retornar
     * seu valor como objeto. O tipo do objeto deve corresponder ao tipo
     * do valor padrão fornecido. Se a flag não for encontrada ou houver erro,
     * deve retornar o valor padrão fornecido.
     * 
     * @param <T> O tipo do objeto retornado
     * @param flagKey A chave única da feature flag
     * @param defaultValue O valor padrão a ser retornado em caso de erro ou flag não encontrada
     * @param context O contexto de avaliação (pode ser null)
     * @return Uma FlagEvaluation contendo o valor do objeto ou o valor padrão
     */
    <T> FlagEvaluation<T> getObjectValue(String flagKey, T defaultValue, EvaluationContext context);
    
    /**
     * Retorna o nome do provedor.
     * 
     * <p>Este nome é usado para identificação e logging.
     * 
     * @return O nome do provedor
     */
    String getName();
    
    /**
     * Retorna o tipo de valor suportado por uma flag específica.
     * 
     * <p>Este método pode ser usado para validar o tipo de uma flag antes
     * de tentar avaliá-la. Se a flag não existir, deve retornar null.
     * 
     * @param flagKey A chave única da feature flag
     * @return O tipo de valor da flag, ou null se a flag não existir
     */
    ValueType getFlagValueType(String flagKey);
    
    /**
     * Verifica se o provedor está pronto para avaliar flags.
     * 
     * <p>Um provedor pode não estar pronto durante a inicialização,
     * reconexão após falha, ou quando há problemas de conectividade.
     * 
     * @return true se o provedor estiver pronto, false caso contrário
     */
    boolean isReady();
}

