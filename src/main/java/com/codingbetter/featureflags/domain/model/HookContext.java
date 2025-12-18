package com.codingbetter.featureflags.domain.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Representa o contexto compartilhado entre hooks durante a avaliação de uma flag.
 * 
 * <p>O HookContext permite que hooks compartilhem dados entre si durante
 * o ciclo de vida da avaliação. Por exemplo, um hook de logging pode armazenar
 * o tempo de início no contexto, e um hook de métricas pode usar esse tempo
 * para calcular a duração da avaliação.
 * 
 * <p>O contexto é thread-safe e pode ser usado por múltiplos hooks simultaneamente.
 * 
 * <p>Exemplo de uso:
 * <pre>{@code
 * HookContext context = new HookContext();
 * context.set("startTime", System.currentTimeMillis());
 * 
 * // ... avaliação da flag ...
 * 
 * long duration = System.currentTimeMillis() - (Long) context.get("startTime");
 * }</pre>
 */
public class HookContext {
    
    private final Map<String, Object> data;
    
    /**
     * Constrói um novo contexto de hook vazio.
     */
    public HookContext() {
        this.data = new ConcurrentHashMap<>();
    }
    
    /**
     * Armazena um valor no contexto.
     * 
     * @param key A chave do valor
     * @param value O valor a ser armazenado
     */
    public void set(String key, Object value) {
        data.put(key, value);
    }
    
    /**
     * Retorna um valor do contexto.
     * 
     * @param key A chave do valor
     * @return O valor armazenado, ou null se não existir
     */
    public Object get(String key) {
        return data.get(key);
    }
    
    /**
     * Retorna um valor do contexto com tipo específico.
     * 
     * @param <T> O tipo esperado do valor
     * @param key A chave do valor
     * @param type A classe do tipo esperado
     * @return O valor armazenado com o tipo especificado, ou null se não existir ou não for do tipo esperado
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = data.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
    
    /**
     * Verifica se uma chave existe no contexto.
     * 
     * @param key A chave a verificar
     * @return true se a chave existir, false caso contrário
     */
    public boolean has(String key) {
        return data.containsKey(key);
    }
    
    /**
     * Remove um valor do contexto.
     * 
     * @param key A chave do valor a remover
     * @return O valor removido, ou null se não existir
     */
    public Object remove(String key) {
        return data.remove(key);
    }
    
    /**
     * Limpa todos os valores do contexto.
     */
    public void clear() {
        data.clear();
    }
    
    /**
     * Retorna todos os dados do contexto como um mapa imutável.
     * 
     * @return Um mapa com todos os dados do contexto
     */
    public Map<String, Object> getAll() {
        return Map.copyOf(data);
    }
}

