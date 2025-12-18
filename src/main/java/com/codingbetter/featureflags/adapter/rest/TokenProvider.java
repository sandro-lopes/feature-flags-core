package com.codingbetter.featureflags.adapter.rest;

/**
 * Interface para fornecer tokens de autenticação quando não há contexto HTTP disponível.
 *
 * <p>Esta interface permite que clientes implementem sua própria estratégia de obtenção
 * de tokens de autenticação para o adapter REST, como fallback quando o header Authorization
 * não está disponível no contexto HTTP da requisição.
 *
 * <p>Use esta interface quando:
 * <ul>
 *   <li>Chamadas são feitas fora de contexto HTTP (jobs, schedulers, testes)</li>
 *   <li>Você precisa de uma estratégia customizada de obtenção de token</li>
 *   <li>O token precisa ser gerado dinamicamente (OAuth2, JWT, etc.)</li>
 * </ul>
 *
 * <p>Exemplo de implementação:
 * <pre>{@code
 * @Component
 * public class MeuTokenProvider implements TokenProvider {
 *     @Override
 *     public String getToken() {
 *         // Obter token de um serviço OAuth2, cache, ou outra fonte
 *         return "Bearer meu-token-gerado";
 *     }
 * }
 * }</pre>
 */
@FunctionalInterface
public interface TokenProvider {

    /**
     * Obtém o token de autenticação.
     *
     * <p>O token retornado deve incluir o prefixo "Bearer " se necessário.
     * O adapter adicionará automaticamente o prefixo se não estiver presente.
     *
     * @return token de autenticação (ex: "Bearer token123" ou "token123"), ou null se não disponível
     */
    String getToken();
}

