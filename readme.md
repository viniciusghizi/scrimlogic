

# ScrimLogic Backend

O **ScrimLogic** é um sistema de backend resiliente focado na ingestão assíncrona, processamento em lote (*batch processing*) e persistência de métricas de partidas competitivas. O sistema foi projetado para lidar com fluxos de dados de diferentes modalidades de eSports (League of Legends e Rocket League).

---

## 🏗 Arquitetura do Sistema

O projeto utiliza o padrão de **Worker**, desacoplando a recepção dos arquivos da persistência de dados, garantindo alta disponibilidade e performance.



### Principais Componentes:
* **File Ingestion Engine:** Monitoramento de diretório (`/inbox`) para detecção e processamento de arquivos JSON.
* **Batch Processor:** Serviço de ETL que realiza a validação, transformação e normalização dos dados.
* **Persistence Layer:** Utiliza **Spring Data JPA** com PostgreSQL e colunas `JSONB` para armazenamento flexível de métricas dinâmicas.

---

## 🚀 Desafios Técnicos Abordados

1.  **Resiliência de Dados:** Tratamento rigoroso de *Null-Safety* em estruturas JSON heterogêneas utilizando `JsonNode.path()` do Jackson, evitando falhas em tempo de execução.
2.  **Conectividade entre Ambientes:** Resolução de problemas de comunicação entre o *Host* (Windows/WSL) e os containers Docker através de mapeamento de portas (`5433:5432`) e configuração de `listen_addresses`.
3.  **Performance de I/O:** Otimização de inserções em banco de dados através do uso de `saveAll()` em operações de lote.

---

## 🛠 Stack Tecnológica

* **Java 21 / Spring Boot**
* **PostgreSQL 16** (com `JSONB`)
* **Docker & Docker Compose**
* **Maven**



---

## ⚙️ Instalação e Configuração

### 1. Pré-requisitos
* Docker Desktop rodando.
* Java 21 JDK e Maven instalados.

### 2. Subindo a infraestrutura
Inicie o banco de dados via Docker Compose:
```bash
docker compose up -d

```

### 3. Configuração

Certifique-se que o arquivo `src/main/resources/application.properties` esteja configurado:

Properties

```
server.port=8082
spring.datasource.url=jdbc:postgresql://localhost:5433/scrimlogic
scrimlogic.inbox.path=./inbox

```

### 4. Executando

Bash

```
mvn clean install
mvn spring-boot:run
```