# Feature Flags - Biblioteca OpenFeature para Java

Uma biblioteca Java que fornece interfaces e contratos baseados no padrão **OpenFeature** para gerenciamento de feature flags. Esta biblioteca foi projetada seguindo os princípios de **Domain-Driven Design (DDD)** e **Arquitetura Hexagonal (Ports & Adapters)**, abstraindo ao máximo a complexidade para o desenvolvedor final.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Características](#características)
- [Arquitetura](#arquitetura)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Conceitos Principais](#conceitos-principais)
- [Interfaces e Contratos](#interfaces-e-contratos)
- [Exemplos de Uso](#exemplos-de-uso)
- [Implementação de Adapters](#implementação-de-adapters)
- [Documentação da API](#documentação-da-api)
- [Contribuindo](#contribuindo)

## 🎯 Sobre o Projeto

Esta biblioteca fornece uma camada de abstração baseada na especificação **OpenFeature** para trabalhar com feature flags em aplicações Java. Ela define contratos claros e bem documentados, além de incluir um adapter REST padrão pronto para uso. Times podem também implementar adapters customizados para se conectar com suas APIs de feature flags específicas.

### Por que OpenFeature?

[OpenFeature](https://openfeature.dev/) é uma especificação open-source que define uma API unificada para feature flags, permitindo que você:

- **Troque de provedor facilmente** sem alterar o código da aplicação
- **Use uma API consistente** independente do sistema de feature flags
- **Teste facilmente** com mocks e stubs
- **Mantenha código limpo** com abstrações bem definidas

## ✨ Características

- ✅ **Baseado na especificação OpenFeature** - Segue os padrões e melhores práticas
- ✅ **Arquitetura DDD + Hexagonal** - Separação clara entre domínio, ports e adapters
- ✅ **Adapter REST padrão incluído** - Implementação pronta para uso com APIs REST
- ✅ **Interfaces bem documentadas** - JavaDoc completo em todos os métodos
- ✅ **Tipos seguros** - Suporte para Boolean, String, Number e Object
- ✅ **Contexto de avaliação** - Suporte completo para targeting e segmentação
- ✅ **Sistema de hooks** - Extensibilidade através de hooks
- ✅ **Tratamento de erros** - Exceções específicas e códigos de erro padronizados
- ✅ **Fácil de usar** - API simplificada para o desenvolvedor final

## 🏗️ Arquitetura

O projeto segue uma arquitetura híbrida combinando **DDD** e **Arquitetura Hexagonal**:

### Arquitetura Hexagonal (Ports & Adapters)

A arquitetura hexagonal separa o núcleo da aplicação (domínio) das tecnologias externas através de **ports** (interfaces) e **adapters** (implementações):

- **Inbound Ports**: Interfaces que o domínio expõe para o mundo externo (ex: `FeatureFlagClient`)
- **Outbound Ports**: Interfaces que o domínio precisa de sistemas externos (ex: `FeatureFlagProvider`)
- **Adapters**: Implementações concretas dos ports (a biblioteca já fornece um adapter REST padrão; times podem implementar adapters customizados conforme necessário)

### Domain-Driven Design (DDD)

O domínio contém:

- **Modelos**: Entidades e Value Objects (`ValueType`, `FlagEvaluation`, `EvaluationContext`, etc.)
- **Exceções**: Exceções específicas do domínio
- **Lógica de negócio pura**: Sem dependências externas

### Diagrama de Arquitetura

```
┌─────────────────────────────────────────────────────────────┐
│                    MUNDO EXTERNO                             │
│  (Aplicações, APIs REST, LaunchDarkly, Split.io, etc.)      │
└──────────────────────┬──────────────────────────────────────┘
                       │
        ┌──────────────┴──────────────┐
        │                             │
        ▼                             ▼
┌───────────────┐            ┌─────────────────┐
│ Inbound       │            │ Outbound        │
│ Adapters      │            │ Adapters        │
│ (Futuro)      │            │                 │
│               │            │ - REST Provider │
│ - REST        │            │   (padrão)      │
│   Controller  │            │ - Customizado   │
│ - CLI         │            │   (opcional)    │
└───────┬───────┘            └────────┬────────┘
        │                             │
        │                             │
        ▼                             ▼
┌─────────────────────────────────────────────┐
│           PORTS (Interfaces)                │
│                                             │
│  ┌──────────────────┐  ┌─────────────────┐│
│  │ Inbound Ports    │  │ Outbound Ports  ││
│  │                  │  │                 ││
│  │ - FeatureFlag    │  │ - FeatureFlag   ││
│  │   Client         │  │   Provider      ││
│  │ - Hook           │  │                 ││
│  └──────────────────┘  └─────────────────┘│
└────────────────────┬───────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────┐
│           DOMAIN (Núcleo)                  │
│                                             │
│  ┌──────────────────────────────────────┐ │
│  │ Modelos (Value Objects)               │ │
│  │ - ValueType                           │ │
│  │ - FlagEvaluation                      │ │
│  │ - FlagMetadata                        │ │
│  │ - ErrorCode                           │ │
│  │ - EvaluationContext                   │ │
│  │ - SimpleEvaluationContext             │ │
│  │ - HookContext                         │ │
│  │ - banking/                            │ │
│  │   - BankingAttributes                 │ │
│  │   - BankingContextBuilder             │ │
│  └──────────────────────────────────────┘ │
│                                             │
│  ┌──────────────────────────────────────┐ │
│  │ Adapters (Infraestrutura)             │ │
│  │ - adapter/rest/                       │ │
│  │   - RestApiFeatureFlagProvider        │ │
│  │   - RestApiFeatureToggleClient        │ │
│  │   - RestApiFeatureToggleConfiguration │ │
│  └──────────────────────────────────────┘ │
│                                             │
│  ┌──────────────────────────────────────┐ │
│  │ Exceções                             │ │
│  │ - FeatureFlagException               │ │
│  │ - FlagNotFoundException              │ │
│  │ - TypeMismatchException              │ │
│  │ - ProviderNotReadyException         │ │
│  │ - EvaluationException                │ │
│  └──────────────────────────────────────┘ │
└─────────────────────────────────────────────┘
```

## 📁 Estrutura do Projeto

O projeto segue uma arquitetura DDD + Hexagonal com a seguinte estrutura:

```
src/main/java/com/codingbetter/featureflags/
├── domain/                       # Núcleo do domínio (DDD)
│   ├── model/                   # Entidades e Value Objects
│   │   ├── ValueType.java
│   │   ├── FlagEvaluation.java
│   │   ├── FlagMetadata.java
│   │   ├── ErrorCode.java
│   │   ├── EvaluationContext.java
│   │   ├── SimpleEvaluationContext.java
│   │   ├── HookContext.java
│   │   └── banking/             # Contextos especializados por domínio
│   │       ├── BankingAttributes.java
│   │       └── BankingContextBuilder.java
│   └── exception/               # Exceções do domínio
│       ├── FeatureFlagException.java
│       ├── FlagNotFoundException.java
│       ├── TypeMismatchException.java
│       ├── ProviderNotReadyException.java
│       └── EvaluationException.java
├── adapter/                     # Adapters (Infraestrutura)
│   └── rest/                    # Adapter REST padrão
│       ├── RestApiFeatureFlagProvider.java
│       ├── RestApiFeatureToggleClient.java
│       ├── RestApiFeatureToggleConfiguration.java
│       ├── RestApiFeatureToggleProperties.java
│       └── dto/
│           └── FuncionalidadeResponse.java
└── port/                        # Ports (Arquitetura Hexagonal)
    ├── inbound/                 # Inbound Ports (driving - interfaces que o domínio expõe)
    │   ├── FeatureFlagClient.java
    │   └── Hook.java
    └── outbound/                # Outbound Ports (driven - interfaces que o domínio precisa)
        └── FeatureFlagProvider.java
```

### Camadas

- **Domain**: Contém os modelos, value objects e exceções do domínio de feature flags (núcleo puro, sem dependências externas)
- **Port/Inbound**: Interfaces que o domínio expõe para o mundo externo (ex: `FeatureFlagClient`, `Hook`)
- **Port/Outbound**: Interfaces que o domínio precisa de sistemas externos (ex: `FeatureFlagProvider`)
- **Adapter**: Implementações concretas dos adapters (ex: `RestApiFeatureFlagProvider` para integração REST)

## 🧠 Conceitos Principais

### Feature Flag Client (Inbound Port)

O `FeatureFlagClient` é a interface principal que os desenvolvedores usam para avaliar feature flags. Ele fornece uma API simples e consistente:

```java
import com.codingbetter.featureflags.port.inbound.FeatureFlagClient;
import com.codingbetter.featureflags.domain.model.SimpleEvaluationContext;

FeatureFlagClient client = // obter instância

// Avaliação simples
boolean enabled = client.getBooleanValue("new-feature", false);

// Avaliação com contexto
EvaluationContext context = SimpleEvaluationContext.builder()
    .targetingKey("user-123")
    .attribute("country", "BR")
    .build();
String theme = client.getStringValue("theme", "default", context);
```

### Feature Flag Provider (Outbound Port)

O `FeatureFlagProvider` é a interface que define o contrato para conectar-se a sistemas de feature flags. A biblioteca já fornece uma implementação padrão (`RestApiFeatureFlagProvider`) para APIs REST, mas times podem implementar adapters customizados para outros provedores (LaunchDarkly, Split.io, etc.). É responsável por:

- Conectar-se ao sistema de feature flags
- Avaliar flags baseado no contexto
- Retornar resultados padronizados
- Tratar erros apropriadamente

### Evaluation Context

O `EvaluationContext` contém informações que influenciam a avaliação de flags:

- **Targeting Key**: Identificador único (usuário, sessão, etc.)
- **Attributes**: Atributos customizados (país, plano, idade, etc.)

### Flag Evaluation

O `FlagEvaluation` encapsula o resultado completo de uma avaliação:

- **Value**: O valor da flag
- **Variant**: A variante retornada (para A/B testing)
- **Reason**: A razão do valor retornado
- **Metadata**: Metadados adicionais
- **Error Information**: Código e mensagem de erro (se houver)

### Hooks (Inbound Port)

Hooks permitem interceptar e estender o comportamento da avaliação:

- **Before**: Antes da avaliação
- **After**: Após avaliação bem-sucedida
- **Error**: Quando ocorre erro
- **Finally**: Sempre ao final

**Exemplo de implementação de Hook para logging:**

```java
package com.seuprojeto.hooks;

import com.codingbetter.featureflags.port.inbound.Hook;
import com.codingbetter.featureflags.domain.model.EvaluationContext;
import com.codingbetter.featureflags.domain.model.FlagEvaluation;
import com.codingbetter.featureflags.domain.model.HookContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingHook implements Hook {

    private static final Logger log = LoggerFactory.getLogger(LoggingHook.class);
    private static final String START_TIME_KEY = "startTime";

    @Override
    public void before(String flagKey, Object defaultValue, EvaluationContext context, HookContext hookContext) {
        // Armazena o tempo de início para calcular duração depois
        hookContext.set(START_TIME_KEY, System.currentTimeMillis());
        
        log.debug("Iniciando avaliação da flag: {} com valor padrão: {}", flagKey, defaultValue);
        
        if (context != null && context.getTargetingKey() != null) {
            log.debug("Targeting key: {}", context.getTargetingKey());
        }
    }

    @Override
    public void after(String flagKey, FlagEvaluation<?> evaluation, HookContext hookContext) {
        Long startTime = hookContext.get(START_TIME_KEY, Long.class);
        long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;
        
        if (evaluation.isSuccess()) {
            log.info("Flag '{}' avaliada com sucesso. Valor: {}, Reason: {}, Variant: {}, Duração: {}ms",
                    flagKey,
                    evaluation.getValue(),
                    evaluation.getReason(),
                    evaluation.getVariant(),
                    duration);
        } else {
            log.warn("Flag '{}' avaliada com erro. ErrorCode: {}, Mensagem: {}, Duração: {}ms",
                    flagKey,
                    evaluation.getErrorCode(),
                    evaluation.getErrorMessage(),
                    duration);
        }
    }

    @Override
    public void error(String flagKey, Exception error, HookContext hookContext) {
        Long startTime = hookContext.get(START_TIME_KEY, Long.class);
        long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;
        
        log.error("Erro ao avaliar flag '{}' após {}ms: {}", flagKey, duration, error.getMessage(), error);
    }

    @Override
    public void finallyAfter(String flagKey, HookContext hookContext) {
        // Limpa dados temporários do contexto
        hookContext.remove(START_TIME_KEY);
        log.trace("Finalizada avaliação da flag: {}", flagKey);
    }

    @Override
    public String getName() {
        return "LoggingHook";
    }
}
```

**Uso do Hook em uma aplicação Spring Boot:**

```java
@Configuration
public class FeatureFlagConfiguration {

    @Bean
    public FeatureFlagClient featureFlagClient(
            FeatureFlagProvider provider,
            List<Hook> hooks) {
        // Hooks serão automaticamente injetados pelo Spring
        return new DefaultFeatureFlagClient(provider, hooks);
    }
}
```

## 🔌 Interfaces e Contratos

### FeatureFlagClient (Inbound Port)

Interface principal para avaliação de feature flags. Fornece métodos para todos os tipos de valores:

```java
package com.codingbetter.featureflags.port.inbound;

public interface FeatureFlagClient {
    // Métodos simplificados (retornam apenas o valor)
    boolean getBooleanValue(String flagKey, boolean defaultValue);
    boolean getBooleanValue(String flagKey, boolean defaultValue, EvaluationContext context);
    
    String getStringValue(String flagKey, String defaultValue);
    String getStringValue(String flagKey, String defaultValue, EvaluationContext context);
    
    double getNumberValue(String flagKey, double defaultValue);
    double getNumberValue(String flagKey, double defaultValue, EvaluationContext context);
    
    <T> T getObjectValue(String flagKey, T defaultValue);
    <T> T getObjectValue(String flagKey, T defaultValue, EvaluationContext context);
    
    // Métodos completos (retornam FlagEvaluation com detalhes)
    FlagEvaluation<Boolean> getBooleanEvaluation(String flagKey, boolean defaultValue, EvaluationContext context);
    FlagEvaluation<String> getStringEvaluation(String flagKey, String defaultValue, EvaluationContext context);
    FlagEvaluation<Double> getNumberEvaluation(String flagKey, double defaultValue, EvaluationContext context);
    <T> FlagEvaluation<T> getObjectEvaluation(String flagKey, T defaultValue, EvaluationContext context);
    
    String getName();
    FeatureFlagProvider getProvider();
}
```

### FeatureFlagProvider (Outbound Port)

Interface que deve ser implementada para conectar-se a sistemas de feature flags:

```java
package com.codingbetter.featureflags.port.outbound;

public interface FeatureFlagProvider {
    FlagEvaluation<Boolean> getBooleanValue(String flagKey, Boolean defaultValue, EvaluationContext context);
    FlagEvaluation<String> getStringValue(String flagKey, String defaultValue, EvaluationContext context);
    FlagEvaluation<Double> getNumberValue(String flagKey, Double defaultValue, EvaluationContext context);
    <T> FlagEvaluation<T> getObjectValue(String flagKey, T defaultValue, EvaluationContext context);
    
    String getName();
    ValueType getFlagValueType(String flagKey);
    boolean isReady();
}
```

### EvaluationContext

Interface para contexto de avaliação:

```java
package com.codingbetter.featureflags.domain.model;

public interface EvaluationContext {
    String getTargetingKey();
    Object getAttribute(String key);
    Map<String, Object> getAttributes();
    boolean hasAttribute(String key);
    
    // Métodos auxiliares com tipos
    String getStringAttribute(String key);
    Integer getIntegerAttribute(String key);
    Boolean getBooleanAttribute(String key);
}
```

## 💡 Exemplos de Uso

### Exemplo 1: Avaliação Simples

```java
import com.codingbetter.featureflags.port.inbound.FeatureFlagClient;

// Obter instância do cliente (implementação será fornecida pelo adapter)
FeatureFlagClient client = // ... obter do seu adapter

// Verificar se uma feature está habilitada
if (client.getBooleanValue("new-checkout-flow", false)) {
    // Usar novo fluxo de checkout
    processNewCheckout();
} else {
    // Usar fluxo antigo
    processOldCheckout();
}
```

### Exemplo 2: Avaliação com Contexto

```java
import com.codingbetter.featureflags.domain.model.SimpleEvaluationContext;
import com.codingbetter.featureflags.port.inbound.FeatureFlagClient;

// Criar contexto com informações do usuário
EvaluationContext context = SimpleEvaluationContext.builder()
    .targetingKey("user-" + userId)
    .attribute("email", user.getEmail())
    .attribute("country", user.getCountry())
    .attribute("plan", user.getPlan())
    .attribute("age", user.getAge())
    .build();

// Avaliar flag com contexto para targeting
String theme = client.getStringValue("ui-theme", "light", context);
int maxItems = (int) client.getNumberValue("cart-max-items", 10.0, context);
```

### Exemplo 3: Avaliação com Detalhes Completos

```java
import com.codingbetter.featureflags.domain.model.FlagEvaluation;

// Obter avaliação completa com metadados
FlagEvaluation<Boolean> evaluation = client.getBooleanEvaluation(
    "premium-feature",
    false,
    context
);

if (evaluation.isSuccess()) {
    boolean enabled = evaluation.getValue();
    String reason = evaluation.getReason(); // "TARGETING_MATCH", "DEFAULT", etc.
    String variant = evaluation.getVariant();
    FlagMetadata metadata = evaluation.getMetadata();
    
    // Usar informações adicionais
    if ("TARGETING_MATCH".equals(reason)) {
        log.info("Feature habilitada por targeting para usuário {}", context.getTargetingKey());
    }
} else {
    ErrorCode errorCode = evaluation.getErrorCode();
    String errorMessage = evaluation.getErrorMessage();
    // Tratar erro
}
```

### Exemplo 4: Avaliação de Objetos com Tipos Específicos

```java
import com.codingbetter.featureflags.port.inbound.FeatureFlagClient;
import com.codingbetter.featureflags.domain.model.FlagEvaluation;

// Definir classe de configuração
public class FeatureConfig {
    private String theme;
    private int maxItems;
    private boolean enabled;
    
    // construtores, getters e setters...
    public FeatureConfig() {}
    
    public FeatureConfig(String theme, int maxItems, boolean enabled) {
        this.theme = theme;
        this.maxItems = maxItems;
        this.enabled = enabled;
    }
    
    // getters e setters...
}

// Uso: O tipo genérico <T> é inferido automaticamente pelo tipo do defaultValue
// Quando você passa um FeatureConfig como defaultValue, o Java infere que T = FeatureConfig

// Criar valor padrão do tipo desejado
FeatureConfig defaultConfig = new FeatureConfig("light", 10, false);

// O tipo T será inferido como FeatureConfig baseado no tipo do defaultValue
FeatureConfig config = client.getObjectValue("feature-config", defaultConfig, context);

// Agora você pode usar o objeto tipado diretamente
if (config.isEnabled()) {
    applyTheme(config.getTheme());
    setMaxItems(config.getMaxItems());
}

// Para obter detalhes completos da avaliação:
FlagEvaluation<FeatureConfig> evaluation = client.getObjectEvaluation(
    "feature-config",
    defaultConfig,
    context
);

if (evaluation.isSuccess()) {
    FeatureConfig config = evaluation.getValue();
    // Usar configuração...
}
```

**Importante**: O tipo genérico `<T>` do método `getObjectValue` é inferido automaticamente pelo compilador Java baseado no tipo do parâmetro `defaultValue`. Quando você passa um objeto de um tipo específico (como `FeatureConfig`) como `defaultValue`, o Java automaticamente infere que `T = FeatureConfig`, permitindo que o método retorne o tipo correto sem necessidade de especificar explicitamente o tipo genérico.

### Exemplo 5: Contexto Bancário Especializado

Para sistemas bancários, a biblioteca fornece um builder especializado com atributos comuns do domínio bancário:

```java
import com.codingbetter.featureflags.port.inbound.FeatureFlagClient;
import com.codingbetter.featureflags.domain.model.banking.BankingContextBuilder;
import com.codingbetter.featureflags.domain.model.banking.BankingAttributes;

// Criar contexto bancário com atributos específicos do domínio
EvaluationContext context = BankingContextBuilder.builder()
    .idConta("conta-12345")
    .idCliente("cliente-67890")
    .codigoAgencia("001")
    .dac("5")
    .tipoConta(BankingAttributes.TipoConta.CORRENTE)
    .segmentoCliente(BankingAttributes.SegmentoCliente.PREMIUM)
    .saldoConta(50000.0)
    .scoreCredito(750)
    .ehPremium(true)
    .canal("mobile")
    .regiao("SUDESTE")
    .atributoCustomizado("tipoOperacao", "transferencia")
    .build();

// Avaliar flags usando o contexto bancário
boolean novoFluxoTransferencia = client.getBooleanValue(
    "novo-fluxo-transferencia", 
    false, 
    context
);

// Verificar limite de transação baseado no segmento e canal
double limiteTransacao = client.getNumberValue(
    "limite-transacao-diaria",
    1000.0,
    context
);

// Obter tema da interface baseado no canal
String temaInterface = client.getStringValue(
    "tema-interface",
    "padrao",
    context
);

// Exemplo: Habilitar funcionalidade apenas para clientes premium no canal mobile
if (context.getStringAttribute(BankingAttributes.SEGMENTO_CLIENTE).equals("premium") 
    && context.getStringAttribute(BankingAttributes.CANAL).equals("mobile")) {
    
    boolean funcionalidadeExclusiva = client.getBooleanValue(
        "funcionalidade-exclusiva-premium-mobile",
        false,
        context
    );
}
```

**Vantagens do BankingContextBuilder**:
- ✅ **Type-safe**: Métodos específicos para cada atributo bancário
- ✅ **Padronização**: Constantes para evitar erros de digitação
- ✅ **Extensível**: Suporte a atributos customizados via `atributoCustomizado()`
- ✅ **Documentado**: JavaDoc completo com exemplos de uso
- ✅ **Flexível**: Pode ser usado em conjunto com `SimpleEvaluationContext`

## 🔧 Implementação de Adapters

A biblioteca segue Arquitetura Hexagonal: o domínio expõe apenas contratos (`FeatureFlagClient`, `FeatureFlagProvider`) e a camada de adapter fornece as implementações concretas.

Atualmente existem duas abordagens:

- **Adapter pronto para uso**: implementação padrão baseada na API REST de Feature Toggles (conforme contrato OpenAPI).
- **Adapters customizados**: cada time pode implementar o próprio adapter seguindo o contrato `FeatureFlagProvider`.

### Adapter Padrão: RestApiFeatureFlagProvider (REST)

Esta biblioteca já fornece um adapter REST pronto, implementado em `RestApiFeatureFlagProvider`, que integra com a API de consulta de funcionalidades via REST.

**Configuração (Spring Boot):**

```yaml
featureflags:
  rest:
    base-url: https://seu-host-interno/feature-toggle
```

**Autenticação com Token:**

O adapter obtém o token de autenticação automaticamente do contexto HTTP da requisição, sem necessidade de passar pelo `EvaluationContext` (o token não faz parte do domínio). A estratégia é:

1. **Header Authorization do HttpServletRequest** (prioridade): O adapter detecta automaticamente o header `Authorization` da requisição HTTP atual
2. **TokenProvider** (fallback opcional): Se não houver header na requisição e um `TokenProvider` estiver configurado, usa o token fornecido por ele

**Exemplo de uso (token automático do contexto HTTP):**

```java
import com.codingbetter.featureflags.port.inbound.FeatureFlagClient;
import com.codingbetter.featureflags.domain.model.EvaluationContext;
import com.codingbetter.featureflags.domain.model.banking.BankingContextBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MinhaAplicacaoController {

    private final FeatureFlagClient featureFlagClient;

    public MinhaAplicacaoController(FeatureFlagClient featureFlagClient) {
        this.featureFlagClient = featureFlagClient;
    }

    @GetMapping("/minha-operacao")
    public void executar() {
        // O adapter REST detecta automaticamente o header Authorization da requisição
        // Não é necessário passar o token no EvaluationContext
        
        EvaluationContext context = BankingContextBuilder.builder()
            .idConta("conta-12345")
            .canal("mobile")
            .build();

        // O token será obtido automaticamente do HttpServletRequest
        boolean habilitada = featureFlagClient.getBooleanValue(
            "minha-jornada-chave",
            false,
            context
        );

        if (habilitada) {
            // executar nova jornada
        }
    }
}
```

**Exemplo: TokenProvider para chamadas fora de contexto HTTP:**

Para chamadas que não estão em contexto de requisição HTTP (ex: jobs, schedulers), implemente `TokenProvider`:

```java
package com.seuprojeto.adapter;

import com.codingbetter.featureflags.adapter.rest.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MeuTokenProvider implements TokenProvider {

    @Value("${feature-toggle.token}")
    private String token;

    @Override
    public String getToken() {
        // Pode retornar token estático, obter de OAuth2, cache, etc.
        return "Bearer " + token;
    }
}
```

```java
// Em um job ou scheduler (sem contexto HTTP)
// O TokenProvider será usado automaticamente como fallback
EvaluationContext context = BankingContextBuilder.builder()
    .idConta("conta-12345")
    .canal("mobile")
    .build();

boolean habilitada = featureFlagClient.getBooleanValue(
    "minha-jornada-chave",
    false,
    context
);
```

**Tratamento de erro quando token não é encontrado:**

Se nenhum token for encontrado (nem do contexto HTTP nem do TokenProvider) e a API exigir autenticação, a requisição falhará com HTTP 401 (Unauthorized), que será tratado como `ErrorCode.NETWORK_ERROR` pelo provider. O valor padrão será retornado.

O adapter REST é registrado automaticamente via configuração Spring (`RestApiFeatureToggleConfiguration`), expondo um `FeatureFlagProvider` pronto para uso. Caso você registre outro `FeatureFlagProvider` na aplicação, a anotação `@ConditionalOnMissingBean` garante que seu provider customizado possa substituir o padrão.

### Adapters Customizados (Outbound Adapter)

Caso seu time utilize outro provedor (LaunchDarkly, Flagsmith, outra API interna, etc.), você pode implementar seu próprio adapter seguindo o contrato `FeatureFlagProvider`:

```java
package com.seuprojeto.adapter;

import com.codingbetter.featureflags.port.outbound.FeatureFlagProvider;
import com.codingbetter.featureflags.domain.model.*;

@Configuration
public class MeuFeatureFlagProvider implements FeatureFlagProvider {
    
    @Override
    public FlagEvaluation<Boolean> getBooleanValue(
            String flagKey, 
            Boolean defaultValue, 
            EvaluationContext context) {
        // Implementação específica do seu provedor
        // ...
    }
    
    // Implementar os demais métodos de acordo com o contrato...
    
    @Override
    public String getName() {
        return "MeuFeatureFlagProvider";
    }
    
    @Override
    public boolean isReady() {
        return true;
    }
}
```

Ao registrar seu próprio `FeatureFlagProvider` como bean Spring, o adapter padrão será automaticamente desabilitado devido ao `@ConditionalOnMissingBean`.

### Implementar FeatureFlagClient (Inbound Adapter)

```java
package com.seuprojeto.adapter;

import com.codingbetter.featureflags.port.inbound.FeatureFlagClient;
import com.codingbetter.featureflags.port.inbound.Hook;
import com.codingbetter.featureflags.port.outbound.FeatureFlagProvider;
import com.codingbetter.featureflags.domain.model.*;

public class DefaultFeatureFlagClient implements FeatureFlagClient {
    
    private final FeatureFlagProvider provider;
    private final List<Hook> hooks;
    
    public DefaultFeatureFlagClient(FeatureFlagProvider provider, List<Hook> hooks) {
        this.provider = provider;
        this.hooks = hooks != null ? new ArrayList<>(hooks) : new ArrayList<>();
    }
    
    @Override
    public boolean getBooleanValue(String flagKey, boolean defaultValue) {
        return getBooleanValue(flagKey, defaultValue, null);
    }
    
    @Override
    public boolean getBooleanValue(String flagKey, boolean defaultValue, EvaluationContext context) {
        FlagEvaluation<Boolean> evaluation = getBooleanEvaluation(flagKey, defaultValue, context);
        return evaluation.getValue();
    }
    
    @Override
    public FlagEvaluation<Boolean> getBooleanEvaluation(
            String flagKey, 
            boolean defaultValue, 
            EvaluationContext context) {
        
        HookContext hookContext = new HookContext();
        
        try {
            // Executar hooks before
            hooks.forEach(hook -> hook.before(flagKey, defaultValue, context, hookContext));
            
            // Avaliar flag
            FlagEvaluation<Boolean> evaluation = provider.getBooleanValue(
                flagKey, 
                defaultValue, 
                context
            );
            
            // Executar hooks after
            hooks.forEach(hook -> hook.after(flagKey, evaluation, hookContext));
            
            return evaluation;
        } catch (Exception e) {
            // Executar hooks error
            hooks.forEach(hook -> hook.error(flagKey, e, hookContext));
            throw new EvaluationException(flagKey, ErrorCode.GENERAL, e.getMessage(), e);
        } finally {
            // Executar hooks finally
            hooks.forEach(hook -> hook.finallyAfter(flagKey, hookContext));
        }
    }
    
    // Implementar outros métodos...
}
```

## 📚 Documentação da API

### Tipos de Valores Suportados

A biblioteca suporta quatro tipos de valores conforme a especificação OpenFeature:

- **BOOLEAN**: Valores verdadeiro/falso
- **STRING**: Valores de texto
- **NUMBER**: Valores numéricos (double)
- **OBJECT**: Valores estruturados (JSON objects)

### Códigos de Erro

A biblioteca define códigos de erro padronizados:

- `FLAG_NOT_FOUND`: Flag não encontrada
- `TYPE_MISMATCH`: Incompatibilidade de tipos
- `PROVIDER_NOT_READY`: Provedor não está pronto
- `GENERAL`: Erro geral
- `PARSE_ERROR`: Erro de parse
- `NETWORK_ERROR`: Erro de rede

### Razões de Avaliação

As razões comuns retornadas em `FlagEvaluation.getReason()`:

- `"DEFAULT"`: Valor padrão foi usado
- `"TARGETING_MATCH"`: Regras de targeting corresponderam
- `"SPLIT"`: Distribuição percentual determinou o valor
- `"DISABLED"`: Flag está desabilitada
- `"ERROR"`: Ocorreu um erro na avaliação

## 🤝 Contribuindo

Contribuições são bem-vindas! Esta biblioteca fornece os contratos, interfaces e um adapter REST padrão. Times podem implementar adapters customizados conforme suas necessidades.

### Próximos Passos

1. Adicionar suporte a cache
2. Implementar hooks padrão (logging, métricas)
3. Adicionar testes unitários para as interfaces
4. Criar exemplos de uso mais detalhados
5. Adicionar suporte a múltiplos adapters simultâneos

## 📖 Referências

- [OpenFeature Specification](https://openfeature.dev/specification/)
- [OpenFeature Java SDK](https://openfeature.dev/docs/reference/sdks/server/java/)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)

## 📄 Licença

Este projeto está licenciado sob a Licença Apache 2.0.

---

**Nota**: Esta biblioteca fornece as interfaces, contratos e um adapter REST padrão. Times podem implementar adapters customizados para conectar-se às suas APIs específicas, seguindo o contrato `FeatureFlagProvider`.
