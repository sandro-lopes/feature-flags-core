package com.codingbetter.featureflags.domain.model.banking;

import com.codingbetter.featureflags.domain.model.EvaluationContext;
import com.codingbetter.featureflags.domain.model.SimpleEvaluationContext;

/**
 * Builder especializado para criar contextos de avaliação relacionados ao sistema bancário.
 * 
 * <p>Este builder facilita a criação de contextos com atributos comuns do domínio bancário,
 * fornecendo métodos type-safe e convenientes.
 * 
 * <p>Exemplo de uso:
 * <pre>{@code
 * EvaluationContext context = BankingContextBuilder.builder()
 *     .idConta("conta-12345")
 *     .idCliente("cliente-67890")
 *     .tipoConta(BankingAttributes.TipoConta.CORRENTE)
 *     .segmentoCliente(BankingAttributes.SegmentoCliente.PREMIUM)
 *     .saldoConta(50000.0)
 *     .scoreCredito(750)
 *     .ehPremium(true)
 *     .regiao("SUDESTE")
 *     .canal("mobile")
 *     .atributoCustomizado("tipoOperacao", "transferencia")
 *     .build();
 * }</pre>
 */
public final class BankingContextBuilder {
    
    private final SimpleEvaluationContext.Builder builder;
    
    private BankingContextBuilder() {
        this.builder = SimpleEvaluationContext.builder();
    }
    
    /**
     * Cria um novo builder para contexto bancário.
     * 
     * @return Um novo builder
     */
    public static BankingContextBuilder builder() {
        return new BankingContextBuilder();
    }
    
    /**
     * Define a chave de targeting usando o ID da conta.
     * 
     * @param idConta O identificador da conta
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder idConta(String idConta) {
        builder.targetingKey(idConta);
        builder.attribute(BankingAttributes.ID_CONTA, idConta);
        return this;
    }
    
    /**
     * Define o ID do cliente.
     * 
     * @param idCliente O identificador do cliente
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder idCliente(String idCliente) {
        builder.attribute(BankingAttributes.ID_CLIENTE, idCliente);
        return this;
    }
    
    /**
     * Define o código da agência.
     * 
     * @param codigoAgencia O código da agência
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder codigoAgencia(String codigoAgencia) {
        builder.attribute(BankingAttributes.CODIGO_AGENCIA, codigoAgencia);
        return this;
    }
    
    /**
     * Define o DAC (Dígito de Auto-Conferência).
     * 
     * @param dac O dígito de auto-conferência
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder dac(String dac) {
        builder.attribute(BankingAttributes.DAC, dac);
        return this;
    }
    
    /**
     * Define o tipo de conta.
     * 
     * @param tipoConta O tipo de conta
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder tipoConta(BankingAttributes.TipoConta tipoConta) {
        builder.attribute(BankingAttributes.TIPO_CONTA, tipoConta.getValor());
        return this;
    }
    
    /**
     * Define o segmento do cliente.
     * 
     * @param segmento O segmento do cliente
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder segmentoCliente(BankingAttributes.SegmentoCliente segmento) {
        builder.attribute(BankingAttributes.SEGMENTO_CLIENTE, segmento.getValor());
        return this;
    }
    
    /**
     * Define o tipo de produto.
     * 
     * @param tipoProduto O tipo de produto
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder tipoProduto(String tipoProduto) {
        builder.attribute(BankingAttributes.TIPO_PRODUTO, tipoProduto);
        return this;
    }
    
    /**
     * Define o saldo da conta.
     * 
     * @param saldo O saldo da conta
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder saldoConta(Double saldo) {
        builder.attribute(BankingAttributes.SALDO_CONTA, saldo);
        return this;
    }
    
    /**
     * Define a renda mensal.
     * 
     * @param renda A renda mensal
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder rendaMensal(Double renda) {
        builder.attribute(BankingAttributes.RENDA_MENSAL, renda);
        return this;
    }
    
    /**
     * Define o score de crédito.
     * 
     * @param score O score de crédito
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder scoreCredito(Integer score) {
        builder.attribute(BankingAttributes.SCORE_CREDITO, score);
        return this;
    }
    
    /**
     * Define o nível de risco.
     * 
     * @param nivelRisco O nível de risco
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder nivelRisco(BankingAttributes.NivelRisco nivelRisco) {
        builder.attribute(BankingAttributes.NIVEL_RISCO, nivelRisco.getValor());
        return this;
    }
    
    /**
     * Define se o cliente é premium.
     * 
     * @param ehPremium true se o cliente é premium, false caso contrário
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder ehPremium(Boolean ehPremium) {
        builder.attribute(BankingAttributes.EH_PREMIUM, ehPremium);
        return this;
    }
    
    /**
     * Define se a conta possui limite de cheque especial.
     * 
     * @param temChequeEspecial true se possui cheque especial, false caso contrário
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder temChequeEspecial(Boolean temChequeEspecial) {
        builder.attribute(BankingAttributes.TEM_CHEQUE_ESPECIAL, temChequeEspecial);
        return this;
    }
    
    /**
     * Define se a conta está ativa.
     * 
     * @param estaAtivo true se a conta está ativa, false caso contrário
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder estaAtivo(Boolean estaAtivo) {
        builder.attribute(BankingAttributes.ESTA_ATIVO, estaAtivo);
        return this;
    }
    
    /**
     * Define o canal de atendimento (ex: mobile, web, agencia, caixa-eletronico).
     * 
     * @param canal O canal de atendimento
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder canal(String canal) {
        builder.attribute(BankingAttributes.CANAL, canal);
        return this;
    }
    
    /**
     * Define a região.
     * 
     * @param regiao A região
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder regiao(String regiao) {
        builder.attribute(BankingAttributes.REGIAO, regiao);
        return this;
    }
    
    /**
     * Define o estado.
     * 
     * @param estado O estado
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder estado(String estado) {
        builder.attribute(BankingAttributes.ESTADO, estado);
        return this;
    }
    
    /**
     * Define a cidade.
     * 
     * @param cidade A cidade
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder cidade(String cidade) {
        builder.attribute(BankingAttributes.CIDADE, cidade);
        return this;
    }
    
    /**
     * Define o limite diário de transações.
     * 
     * @param limite O limite diário de transações
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder limiteTransacaoDiaria(Double limite) {
        builder.attribute(BankingAttributes.LIMITE_TRANSACAO_DIARIA, limite);
        return this;
    }
    
    /**
     * Define o limite mensal de transações.
     * 
     * @param limite O limite mensal de transações
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder limiteTransacaoMensal(Double limite) {
        builder.attribute(BankingAttributes.LIMITE_TRANSACAO_MENSAL, limite);
        return this;
    }
    
    /**
     * Adiciona um atributo customizado.
     * Útil para adicionar atributos específicos não cobertos pelos métodos acima.
     * 
     * @param chave A chave do atributo
     * @param valor O valor do atributo
     * @return Este builder para encadeamento
     */
    public BankingContextBuilder atributoCustomizado(String chave, Object valor) {
        builder.attribute(chave, valor);
        return this;
    }
    
    /**
     * Constrói o contexto de avaliação.
     * 
     * @return Uma instância de EvaluationContext com os atributos configurados
     */
    public EvaluationContext build() {
        return builder.build();
    }
}

