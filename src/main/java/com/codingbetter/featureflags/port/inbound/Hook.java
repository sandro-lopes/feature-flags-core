package com.codingbetter.featureflags.port.inbound;

import com.codingbetter.featureflags.domain.model.EvaluationContext;
import com.codingbetter.featureflags.domain.model.FlagEvaluation;
import com.codingbetter.featureflags.domain.model.HookContext;

/**
 * Interface que define hooks para interceptar e estender o comportamento
 * da avaliação de feature flags (Inbound Port).
 * 
 * <p>Hooks permitem adicionar funcionalidades transversais como:
 * <ul>
 *   <li>Logging de avaliações</li>
 *   <li>Métricas e monitoramento</li>
 *   <li>Validação de contexto</li>
 *   <li>Transformação de valores</li>
 *   <li>Cache de resultados</li>
 * </ul>
 * 
 * <p>Conforme a especificação OpenFeature, hooks são executados em diferentes
 * pontos do ciclo de vida da avaliação:
 * <ul>
 *   <li>Before - antes da avaliação</li>
 *   <li>After - depois da avaliação bem-sucedida</li>
 *   <li>Error - quando ocorre um erro</li>
 *   <li>Finally - sempre ao final (sucesso ou erro)</li>
 * </ul>
 * 
 * <p>Implementações podem escolher quais métodos implementar.
 * Métodos não implementados não terão efeito.
 * 
 * @see <a href="https://openfeature.dev/specification/sections/hooks">OpenFeature Specification - Hooks</a>
 */
public interface Hook {
    
    /**
     * Executado antes da avaliação de uma flag.
     * 
     * <p>Este método pode ser usado para:
     * <ul>
     *   <li>Validar ou modificar o contexto</li>
     *   <li>Registrar informações para logging</li>
     *   <li>Incrementar contadores de métricas</li>
     * </ul>
     * 
     * @param flagKey A chave da flag sendo avaliada
     * @param defaultValue O valor padrão que será usado
     * @param context O contexto de avaliação
     * @param hookContext Contexto específico do hook (pode ser usado para passar dados entre hooks)
     */
    default void before(String flagKey, Object defaultValue, EvaluationContext context, HookContext hookContext) {
        // Implementação padrão vazia - hooks podem optar por não implementar
    }
    
    /**
     * Executado após uma avaliação bem-sucedida.
     * 
     * <p>Este método pode ser usado para:
     * <ul>
     *   <li>Registrar o resultado da avaliação</li>
     *   <li>Atualizar métricas de sucesso</li>
     *   <li>Armazenar resultados em cache</li>
     * </ul>
     * 
     * @param flagKey A chave da flag avaliada
     * @param evaluation O resultado da avaliação
     * @param hookContext Contexto específico do hook
     */
    default void after(String flagKey, FlagEvaluation<?> evaluation, HookContext hookContext) {
        // Implementação padrão vazia - hooks podem optar por não implementar
    }
    
    /**
     * Executado quando ocorre um erro durante a avaliação.
     * 
     * <p>Este método pode ser usado para:
     * <ul>
     *   <li>Registrar erros para análise</li>
     *   <li>Atualizar métricas de erro</li>
     *   <li>Notificar sistemas de monitoramento</li>
     * </ul>
     * 
     * @param flagKey A chave da flag que falhou
     * @param error O erro que ocorreu
     * @param hookContext Contexto específico do hook
     */
    default void error(String flagKey, Exception error, HookContext hookContext) {
        // Implementação padrão vazia - hooks podem optar por não implementar
    }
    
    /**
     * Executado sempre ao final da avaliação, independente de sucesso ou erro.
     * 
     * <p>Este método pode ser usado para:
     * <ul>
     *   <li>Limpeza de recursos</li>
     *   <li>Finalização de métricas</li>
     *   <li>Garantir que ações sejam executadas</li>
     * </ul>
     * 
     * @param flagKey A chave da flag avaliada
     * @param hookContext Contexto específico do hook
     */
    default void finallyAfter(String flagKey, HookContext hookContext) {
        // Implementação padrão vazia - hooks podem optar por não implementar
    }
    
    /**
     * Retorna o nome do hook.
     * 
     * <p>Útil para identificação e logging.
     * 
     * @return O nome do hook
     */
    String getName();
}

