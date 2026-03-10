# Changelog - ScrimLogic Backend

Todas as alterações notáveis neste projeto serão documentadas neste arquivo.
O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto segue o Versionamento Semântico.

---

## [1.1.0] - 2026-03-10

### Adicionado
- **Suporte a Rocket League:** Implementação completa da ingestão e processamento de métricas para a modalidade Rocket League.
- **Arquitetura Plug-and-Play:** Introdução dos padrões de projeto **Strategy** e **Factory** para isolar as regras de negócio de cada jogo, permitindo expansão sem alteração do código core.
- **Processamento em Lote Otimizado:** Implementação do uso de `saveAll()` para persistência eficiente de múltiplas partidas em uma única transação de banco de dados.

### Alterado
- **Refatoração SOLID:** Migração da lógica de processamento centralizada para componentes especializados por modalidade, aumentando a testabilidade e a facilidade de manutenção.

### Técnico
- Implementação de polimorfismo na camada de serviço para seleção dinâmica da estratégia de processamento através da Factory.
- Otimização do mapeamento JPA para suporte a campos específicos de Rocket League dentro da estrutura `JSONB`.

---

## [1.0.0] - 2026-03-09

### Adicionado
- **Arquitetura Base:** Estrutura inicial do projeto Spring Boot 3.x com Java 21.
- **Ingestão:** Implementação do `FileIngestionService` com monitoramento de diretório (`./inbox`).
- **Processamento:** Implementação do `ScrimBatchProcessor` para ETL de arquivos JSON.
- **Persistência:** Integração com PostgreSQL 16 utilizando JPA e suporte a colunas `JSONB` para métricas dinâmicas.
- **Configuração:** Infraestrutura Dockerizada com suporte a porta customizada (5433).
- **Documentação:** README oficial do projeto com arquitetura e guias de instalação.

### Corrigido
- **Resiliência:** Implementado `JsonNode.path()` para evitar `NullPointerException` durante o parsing de JSON heterogêneo.
- **Conectividade:** Ajuste de porta e `listen_addresses` no PostgreSQL para permitir conexões do ambiente Host (Windows/WSL).
- **Configuração de Servidor:** Mudança da porta do Spring Boot para `8082` evitando conflitos de rede.
- **Bean Management:** Criação do `BeanConfig` para injeção correta do `ObjectMapper` (Jackson).

### Técnico
- Adicionada dependência `jackson-datatype-jsr310` para suporte a tipos de data/hora do Java 8+.
- Configuração do `ddl-auto=update` no Hibernate para gerenciamento automático do schema.