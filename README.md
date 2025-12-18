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

Esta biblioteca fornece uma camada de abstração baseada na especificação **OpenFeature** para trabalhar com feature flags em aplicações Java. Ela define contratos claros e bem documentados que permitem que diferentes projetos implementem adapters para se conectar com suas APIs REST de feature flags preferidas.

### Por que OpenFeature?

[OpenFeature](https://openfeature.dev/) é uma especificação open-source que define uma API unificada para feature flags, permitindo que você:

- **Troque de provedor facilmente** sem alterar o código da aplicação
- **Use uma API consistente** independente do sistema de feature flags
- **Teste facilmente** com mocks e stubs
- **Mantenha código limpo** com abstrações bem definidas

## ✨ Características

- ✅ **Baseado na especificação OpenFeature** - Segue os padrões e melhores práticas
- ✅ **Arquitetura DDD + Hexagonal** - Separação clara entre domínio, ports e adapters
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
- **Adapters**: Implementações concretas dos ports (a serem implementados pelos usuários)

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
│ (Futuro)      │            │ (Você implementa)│
│               │            │                 │
│ - REST        │            │ - REST Provider │
│   Controller  │            │ - LaunchDarkly  │
│ - CLI         │            │ - Split.io      │
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
├── FeatureFlagsApplication.java  # Classe principal Spring Boot
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
- **Infrastructure**: (A ser implementado pelos usuários) Adapters para APIs REST específicas

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

O `FeatureFlagProvider` é a interface que deve ser implementada para conectar-se a um sistema de feature flags específico (API REST, LaunchDarkly, Split.io, etc.). É responsável por:

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

Para usar esta biblioteca, você precisará implementar os adapters na camada de infraestrutura. Aqui está um guia básico:

### Passo 1: Implementar FeatureFlagProvider (Outbound Adapter)

```java
package com.seuprojeto.infrastructure.adapter;

import com.codingbetter.featureflags.port.outbound.FeatureFlagProvider;
import com.codingbetter.featureflags.domain.model.*;

public class RestApiFeatureFlagProvider implements FeatureFlagProvider {
    
    private final RestClient restClient;
    private final String apiUrl;
    
    public RestApiFeatureFlagProvider(RestClient restClient, String apiUrl) {
        this.restClient = restClient;
        this.apiUrl = apiUrl;
    }
    
    @Override
    public FlagEvaluation<Boolean> getBooleanValue(
            String flagKey, 
            Boolean defaultValue, 
            EvaluationContext context) {
        
        try {
            // Fazer chamada REST para sua API
            FlagResponse response = restClient.get(apiUrl + "/flags/" + flagKey, context);
            
            // Converter resposta para FlagEvaluation
            return new FlagEvaluation<>(
                response.getBooleanValue(),
                response.getVariant(),
                response.getReason(),
                new FlagMetadata(response.getMetadata())
            );
        } catch (Exception e) {
            return new FlagEvaluation<>(
                defaultValue,
                ErrorCode.NETWORK_ERROR,
                e.getMessage(),
                new FlagMetadata()
            );
        }
    }
    
    // Implementar outros métodos...
    
    @Override
    public String getName() {
        return "RestApiProvider";
    }
    
    @Override
    public boolean isReady() {
        // Verificar se a conexão está ativa
        return restClient.isConnected();
    }
}
```

### Passo 2: Implementar FeatureFlagClient (Inbound Adapter)

```java
package com.seuprojeto.infrastructure.adapter;

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

### Passo 3: Configurar e Usar

```java
// Criar provider (outbound adapter)
RestClient restClient = new RestClient();
FeatureFlagProvider provider = new RestApiFeatureFlagProvider(restClient, "https://api.example.com");

// Criar hooks (opcional)
List<Hook> hooks = Arrays.asList(
    new LoggingHook(),
    new MetricsHook()
);

// Criar client (inbound adapter)
FeatureFlagClient client = new DefaultFeatureFlagClient(provider, hooks);

// Usar
boolean enabled = client.getBooleanValue("my-feature", false);
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

Contribuições são bem-vindas! Esta biblioteca fornece apenas os contratos e interfaces. As implementações dos adapters devem ser criadas em projetos separados ou na camada de infraestrutura.

### Próximos Passos

1. Implementar adapters para APIs REST específicas
2. Adicionar suporte a cache
3. Implementar hooks padrão (logging, métricas)
4. Adicionar testes unitários para as interfaces
5. Criar exemplos de uso mais detalhados

## 📖 Referências

- [OpenFeature Specification](https://openfeature.dev/specification/)
- [OpenFeature Java SDK](https://openfeature.dev/docs/reference/sdks/server/java/)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)

## 📄 Licença

Este projeto está licenciado sob a Licença Apache 2.0.

---

**Nota**: Esta biblioteca fornece apenas as interfaces e contratos. As implementações dos adapters devem ser criadas para conectar-se às suas APIs REST específicas.
