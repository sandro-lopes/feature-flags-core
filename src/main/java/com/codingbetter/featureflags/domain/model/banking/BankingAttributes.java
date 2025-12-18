package com.codingbetter.featureflags.domain.model.banking;

/**
 * Constantes para atributos comuns relacionados ao sistema bancário.
 * 
 * <p>Esta classe fornece chaves padronizadas para atributos frequentemente
 * usados em contextos de avaliação de feature flags em sistemas bancários.
 * 
 * <p>Exemplo de uso:
 * <pre>{@code
 * EvaluationContext context = BankingContextBuilder.builder()
 *     .idConta("conta-12345")
 *     .tipoConta(TipoConta.CORRENTE)
 *     .segmentoCliente(SegmentoCliente.PREMIUM)
 *     .codigoAgencia("001")
 *     .canal("mobile")
 *     .atributoCustomizado("tipoOperacao", "transferencia")
 *     .build();
 * }</pre>
 */
public final class BankingAttributes {
    
    private BankingAttributes() {
        // Classe utilitária - não instanciável
    }
    
    // Identificadores
    public static final String ID_CONTA = "idConta";
    public static final String ID_CLIENTE = "idCliente";
    public static final String CODIGO_AGENCIA = "codigoAgencia";
    public static final String DAC = "dac";
    
    // Classificações
    public static final String TIPO_CONTA = "tipoConta";
    public static final String SEGMENTO_CLIENTE = "segmentoCliente";
    public static final String TIPO_PRODUTO = "tipoProduto";
    
    // Características financeiras
    public static final String SALDO_CONTA = "saldoConta";
    public static final String RENDA_MENSAL = "rendaMensal";
    public static final String SCORE_CREDITO = "scoreCredito";
    public static final String NIVEL_RISCO = "nivelRisco";
    
    // Status e flags
    public static final String EH_PREMIUM = "ehPremium";
    public static final String TEM_CHEQUE_ESPECIAL = "temChequeEspecial";
    public static final String ESTA_ATIVO = "estaAtivo";
    
    // Canais de atendimento
    public static final String CANAL = "canal";
    
    // Localização e região
    public static final String REGIAO = "regiao";
    public static final String ESTADO = "estado";
    public static final String CIDADE = "cidade";
    
    // Operações e limites
    public static final String LIMITE_TRANSACAO_DIARIA = "limiteTransacaoDiaria";
    public static final String LIMITE_TRANSACAO_MENSAL = "limiteTransacaoMensal";
    
    /**
     * Enum para tipos de conta bancária.
     */
    public enum TipoConta {
        CORRENTE("corrente"),
        POUPANCA("poupanca"),
        INVESTIMENTO("investimento"),
        EMPRESARIAL("empresarial");
        
        private final String valor;
        
        TipoConta(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
    }
    
    /**
     * Enum para segmentos de cliente.
     */
    public enum SegmentoCliente {
        BASICO("basico"),
        STANDARD("standard"),
        PREMIUM("premium"),
        PRIVATE("private");
        
        private final String valor;
        
        SegmentoCliente(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
    }
    
    /**
     * Enum para níveis de risco.
     */
    public enum NivelRisco {
        BAIXO("baixo"),
        MEDIO("medio"),
        ALTO("alto");
        
        private final String valor;
        
        NivelRisco(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
    }
}

