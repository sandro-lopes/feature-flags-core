package com.codingbetter.featureflags.domain.model;

/**
 * Representa o resultado da avaliação de uma feature flag.
 * 
 * <p>Esta classe encapsula todas as informações retornadas pela avaliação de uma flag,
 * incluindo o valor, detalhes sobre a avaliação e metadados adicionais.
 * 
 * <p>Conforme a especificação OpenFeature, uma avaliação de flag deve incluir:
 * <ul>
 *   <li>O valor da flag (que pode ser de diferentes tipos)</li>
 *   <li>Detalhes sobre como a avaliação foi realizada</li>
 *   <li>Metadados adicionais sobre a flag</li>
 * </ul>
 * 
 * @param <T> O tipo do valor retornado pela flag
 * 
 * @see <a href="https://openfeature.dev/specification/types/#flag-evaluation">OpenFeature Specification - Flag Evaluation</a>
 */
public class FlagEvaluation<T> {
    
    private final T value;
    private final String variant;
    private final String reason;
    private final FlagMetadata metadata;
    private final ErrorCode errorCode;
    private final String errorMessage;
    
    /**
     * Constrói uma avaliação de flag bem-sucedida.
     * 
     * @param value O valor da flag
     * @param variant A variante da flag (opcional)
     * @param reason A razão pela qual este valor foi retornado
     * @param metadata Metadados adicionais sobre a flag
     */
    public FlagEvaluation(T value, String variant, String reason, FlagMetadata metadata) {
        this.value = value;
        this.variant = variant;
        this.reason = reason;
        this.metadata = metadata;
        this.errorCode = null;
        this.errorMessage = null;
    }
    
    /**
     * Constrói uma avaliação de flag com erro.
     * 
     * @param value O valor padrão a ser retornado
     * @param errorCode O código do erro ocorrido
     * @param errorMessage A mensagem de erro
     * @param metadata Metadados adicionais sobre a flag
     */
    public FlagEvaluation(T value, ErrorCode errorCode, String errorMessage, FlagMetadata metadata) {
        this.value = value;
        this.variant = null;
        this.reason = "ERROR";
        this.metadata = metadata;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    /**
     * Retorna o valor da flag.
     * 
     * @return O valor da flag (pode ser o valor padrão em caso de erro)
     */
    public T getValue() {
        return value;
    }
    
    /**
     * Retorna a variante da flag.
     * 
     * <p>A variante identifica qual variação específica da flag foi retornada.
     * Por exemplo, "red", "blue", "green" para uma flag de tema.
     * 
     * @return A variante da flag, ou null se não aplicável
     */
    public String getVariant() {
        return variant;
    }
    
    /**
     * Retorna a razão pela qual este valor foi retornado.
     * 
     * <p>Valores comuns incluem:
     * <ul>
     *   <li>"DEFAULT" - valor padrão foi usado</li>
     *   <li>"TARGETING_MATCH" - targeting rules corresponderam</li>
     *   <li>"SPLIT" - distribuição percentual determinou o valor</li>
     *   <li>"DISABLED" - flag está desabilitada</li>
     *   <li>"ERROR" - ocorreu um erro na avaliação</li>
     * </ul>
     * 
     * @return A razão da avaliação
     */
    public String getReason() {
        return reason;
    }
    
    /**
     * Retorna os metadados da flag.
     * 
     * @return Os metadados da flag
     */
    public FlagMetadata getMetadata() {
        return metadata;
    }
    
    /**
     * Retorna o código do erro, se houver.
     * 
     * @return O código do erro, ou null se não houver erro
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    /**
     * Retorna a mensagem de erro, se houver.
     * 
     * @return A mensagem de erro, ou null se não houver erro
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Verifica se a avaliação foi bem-sucedida.
     * 
     * @return true se não houver erro, false caso contrário
     */
    public boolean isSuccess() {
        return errorCode == null;
    }
    
    /**
     * Verifica se a avaliação resultou em erro.
     * 
     * @return true se houver erro, false caso contrário
     */
    public boolean hasError() {
        return errorCode != null;
    }
}

