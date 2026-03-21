# 💳 Serviço de Pagamentos — Revenda de Veículos

[![CI/CD Pipeline](https://github.com/yudi19/servico-revenda-veiculos-pagamentos/actions/workflows/deploy.yml/badge.svg)](https://github.com/yudi19/servico-revenda-veiculos-pagamentos/actions/workflows/deploy.yml)
[![SonarQube](https://github.com/yudi19/servico-revenda-veiculos-pagamentos/actions/workflows/sonar-pr.yml/badge.svg)](https://github.com/yudi19/servico-revenda-veiculos-pagamentos/actions/workflows/sonar-pr.yml)

Microserviço responsável pelo processamento de vendas e pagamentos da plataforma de revenda. Desenvolvido com **Java 21**, **Spring Boot 4** e arquitetura hexagonal, orquestra o processo de venda consultando o **serviço de listagem** para obter dados do veículo e processando confirmações de pagamento via webhook.

## 📋 Sumário

- [Sobre o Projeto](#-sobre-o-projeto)
- [Arquitetura](#-arquitetura)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
  - [Executando Localmente](#executando-localmente)
  - [Executando com Docker](#executando-com-docker)
- [Documentação da API](#-documentação-da-api)
- [Testes](#-testes)
- [CI/CD](#-cicd)
- [Infraestrutura](#-infraestrutura)

## 🎯 Sobre o Projeto

O serviço de pagamentos coordena as transações de venda de veículos. Ao receber uma solicitação de venda, consulta o **serviço de listagem** via HTTP para validar o veículo e obter seus dados, registra a venda e aguarda a confirmação do pagamento através de um webhook. Suporta o ciclo completo: `PENDENTE → APROVADO` ou `PENDENTE → CANCELADO`.

### Funcionalidades Principais

- ✅ Efetuar venda com validação de veículo via serviço de listagem
- ✅ Geração de código de pagamento único
- ✅ Recebimento de confirmação/cancelamento via **webhook**
- ✅ Listagem de veículos vendidos
- ✅ Validação de CPF do comprador

## 🏗 Arquitetura

O projeto segue os princípios da Arquitetura Hexagonal:

```
src/main/java/com/revendas/revendas/pagamentos/
├── domain/                          # Camada de Domínio
│   ├── model/                       # Entidades: Venda, StatusPagamento
│   └── exception/                   # VendaNaoEncontradaException
│                                    # VeiculoNaoDisponivelException
├── application/                     # Camada de Aplicação
│   ├── port/
│   │   ├── in/                      # Casos de uso (interfaces)
│   │   └── out/                     # VeiculoServicePort, VendaRepositoryPort
│   └── service/                     # VendaService
└── adapter/                         # Camada de Infraestrutura
    ├── in/
    │   └── web/                     # Controller REST + DTOs + Mapper
    └── out/
        ├── persistence/             # Repositório JPA (H2)
        └── http/                    # VeiculoHttpAdapter (REST client)
```

### Camadas

- **Domain**: Entidade `Venda` com dados do veículo, comprador, código de pagamento e status
- **Application**: Casos de uso `EfetuarVenda`, `ProcessarWebhook`, `ListarVendidas`
- **Adapter IN**: Controller REST (`/vendas`) com validação via Bean Validation
- **Adapter OUT**: Repositório JPA (H2) + `VeiculoHttpAdapter` que consome o serviço de listagem via REST
- **Infrastructure**: `RestTemplateConfig` com configuração do cliente HTTP

### Fluxo de Venda

```
POST /vendas
  │
  ├── VeiculoHttpAdapter.buscarVeiculo(veiculoId)
  │     └── GET http://servico-listagem:8080/veiculos/{id}
  │
  ├── Cria Venda com status PENDENTE
  │     └── Gera codigoPagamento único
  │
  └── POST /vendas/webhook { codigoPagamento, status: APROVADO }
        └── Atualiza Venda → APROVADO ou CANCELADO
```

## 🛠 Tecnologias

- **Java 21** — Linguagem de programação
- **Spring Boot 4** — Framework principal
- **Spring Data JPA** — Persistência de dados
- **H2 Database** — Banco de dados em memória
- **RestTemplate** — Comunicação HTTP com o serviço de listagem
- **Lombok** — Redução de boilerplate
- **Gradle** — Gerenciamento de dependências e build
- **Docker** — Containerização
- **GitHub Actions** — CI/CD
- **JUnit 5 + Mockito** — Testes unitários
- **JaCoCo** — Cobertura de testes (mínimo 80%)

## 📦 Pré-requisitos

- **Java 21** ou superior
- **Gradle 8+** (ou use o wrapper incluído)
- **Docker** (opcional)
- **Serviço de Listagem** em execução (configurado via `listagem.service.url`)

## 🚀 Instalação e Execução

### Executando Localmente

1. **Clone o repositório:**
```bash
git clone https://github.com/yudi19/servico-revenda-veiculos-pagamentos.git
cd servico-revenda-veiculos-pagamentos
```

2. **Certifique-se de que o serviço de listagem está rodando em `http://localhost:8080`** (ou ajuste `listagem.service.url` no `application.properties`).

3. **Execute a aplicação:**
```bash
./gradlew bootRun
```

A API estará disponível em `http://localhost:8081`  
Console H2: `http://localhost:8081/h2-console`

### Executando com Docker

1. **Build da imagem:**
```bash
docker build -t servico-revenda-veiculos-pagamentos .
```

2. **Execute o container:**
```bash
docker run -p 8081:8081 \
  -e LISTAGEM_SERVICE_URL=http://host.docker.internal:8080 \
  servico-revenda-veiculos-pagamentos
```

## 📚 Documentação da API

### Base URL
```
Local:      http://localhost:8081
Produção:   http://<ECS_PUBLIC_IP>:8081
```

---

### 💳 Endpoints de Vendas

#### 1. Efetuar Venda

```http
POST /vendas
Content-Type: application/json
```

**Corpo da Requisição:**
```json
{
  "veiculoId": 1,
  "cpfComprador": "123.456.789-09",
  "dataVenda": "2026-03-21"
}
```

**Resposta de Sucesso (201 Created):**
```json
{
  "id": 1,
  "veiculoId": 1,
  "marcaVeiculo": "Toyota",
  "modeloVeiculo": "Corolla",
  "anoVeiculo": 2023,
  "precoVeiculo": 120000.00,
  "cpfComprador": "123.456.789-09",
  "dataVenda": "2026-03-21",
  "codigoPagamento": "PAG-A1B2C3D4",
  "statusPagamento": "PENDENTE"
}
```

---

#### 2. Webhook de Confirmação de Pagamento

Endpoint chamado pelo gateway de pagamento para confirmar ou cancelar a transação.

```http
POST /vendas/webhook
Content-Type: application/json
```

**Corpo da Requisição:**
```json
{
  "codigoPagamento": "PAG-A1B2C3D4",
  "status": "APROVADO"
}
```

**Valores aceitos para `status`:** `APROVADO` | `CANCELADO`

**Resposta de Sucesso (200 OK):** objeto `Venda` atualizado com o novo status.

---

#### 3. Listar Veículos Vendidos

Retorna todas as vendas com status `APROVADO`.

```http
GET /vendas/vendidos
```

**Resposta de Sucesso (200 OK):** array com todas as vendas aprovadas.

**Status possíveis:** `PENDENTE` | `APROVADO` | `CANCELADO`

---

### Exemplos com cURL

```bash
BASE="http://localhost:8081"

# Efetuar venda
curl -s -X POST "$BASE/vendas" \
  -H "Content-Type: application/json" \
  -d '{"veiculoId":1,"cpfComprador":"123.456.789-09","dataVenda":"2026-03-21"}'

# Confirmar pagamento via webhook
curl -s -X POST "$BASE/vendas/webhook" \
  -H "Content-Type: application/json" \
  -d '{"codigoPagamento":"PAG-A1B2C3D4","status":"APROVADO"}'

# Cancelar pagamento via webhook
curl -s -X POST "$BASE/vendas/webhook" \
  -H "Content-Type: application/json" \
  -d '{"codigoPagamento":"PAG-A1B2C3D4","status":"CANCELADO"}'

# Listar vendas aprovadas
curl -s "$BASE/vendas/vendidos"
```

## 🧪 Testes

```bash
# Executar todos os testes
./gradlew test

# Relatório de cobertura (JaCoCo)
open build/reports/jacoco/test/html/index.html
```

Cobertura de testes unitários nas camadas:
- **Domain Model**: campos e construção da entidade `Venda`
- **Application Service**: todos os casos de uso com Mockito (incluindo mock do `VeiculoServicePort`)
- **Controller**: testes de integração com MockMvc
- **Persistence Adapter**: mapeamento e persistência JPA

## 🔄 CI/CD

Pipeline automatizado via GitHub Actions:

- Ao **abrir PR para `main`** (`.github/workflows/sonar-pr.yml`):
  - ✅ Build e execução dos testes
  - ✅ Geração do relatório JaCoCo
  - ✅ Análise de qualidade no **SonarQube**

- Ao **fazer merge na `main`** (`.github/workflows/deploy.yml`):
  - ✅ Build com Gradle
  - ✅ Execução dos testes
  - ✅ Build e push da imagem Docker para **Amazon ECR**
  - ✅ Deploy automático no **Amazon ECS Fargate**
  - ✅ Aguarda estabilização do serviço

### Secrets necessários no repositório

| Secret | Descrição |
|--------|----------|
| `AWS_ACCESS_KEY_ID` | Access key com permissões ECR + ECS |
| `AWS_SECRET_ACCESS_KEY` | Secret key correspondente |
| `SONAR_TOKEN` | Token de autenticação do SonarCloud |
| `SONAR_HOST_URL` | URL do servidor Sonar (ex: `https://sonarcloud.io`) |

## ☁️ Infraestrutura

**Recursos AWS:**
- **ECS Cluster**: `revendas-cluster`
- **ECS Service**: `servico-revenda-veiculos-pagamentos`
- **ECR Repository**: `servico-revenda-veiculos-pagamentos`
- **CloudWatch Logs**: `/ecs/servico-pagamentos`
- **Região**: `us-east-1`

> O serviço depende do `servico-revenda-veiculos-listagem` para consultar dados do veículo. A URL é configurada via variável de ambiente `LISTAGEM_SERVICE_URL`.

## 📄 Licença

Este projeto foi desenvolvido como trabalho acadêmico para a Pós-Tech FIAP.

## 👥 Autores

- **Fabio** — [@yudi19](https://github.com/yudi19)