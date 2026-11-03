# 🍽️ Restaurant Management System — FIAP Tech Challenge (Fase 2)

> Pós-Graduação em Arquitetura e Desenvolvimento Java — FIAP / Postech

API REST para um sistema compartilhado de gestão de restaurantes. Nesta fase o serviço passa a cadastrar tipos de usuário, associá-los a usuários existentes, cadastrar restaurantes e os itens de cardápio vendidos por cada estabelecimento.

## 👨‍💻 Autores

* **Marcos Ruiz Freire** (`marcosruizfreire@gmail.com`)
* **José Bernardino da Silva Júnior** (`jbernardino.jr@gmail.com`)
* **Lucas Brisolla** (`lucasgbrisolla@gmail.com`)
* **Rodrigo Fittipaldi** (`rodrfit@gmail.com`)

**Repositório:** [github.com/rodrfit/restaurant-management](https://github.com/rodrfit/restaurant-management)

## 📜 1. Introdução

Este documento descreve a solução da Fase 2 do Tech Challenge. O sistema é um backend compartilhado para um grupo de restaurantes. A Fase 1 entregou o módulo de usuários. Esta fase acrescenta o cadastro de tipos de usuário, a associação desse tipo a um usuário existente, o cadastro de restaurantes e o cadastro dos itens de cardápio.

A aplicação é Java 21 com Spring Boot 4.1, MySQL 8, Docker Compose, Swagger/OpenAPI, coleção Postman e erros no formato ProblemDetail (RFC 7807). A API é versionada em `/api/v1`.

## 🎯 2. Problema

Vários restaurantes da região precisavam de um sistema único de gestão, em vez de contratar um produto separado para cada casa. O cliente escolhe o restaurante pela comida. O sistema precisa deixar o dono operar o estabelecimento e o cliente consultar o que está disponível.

A entrega é faseada. Nesta fase o escopo é:

* 🏷️ **Tipo de usuário:** nome do tipo e associação a um usuário já cadastrado. Os perfis previstos são Dono de Restaurante e Cliente.
* 🏪 **Restaurante:** nome, endereço, tipo de cozinha, horário de funcionamento e dono (um usuário existente).
* 🍕 **Item de cardápio:** nome, descrição, preço, indicação de consumo apenas no restaurante e caminho da foto do prato. O arquivo da foto não é armazenado.

## 🏗️ 3. Arquitetura

Clean Architecture em três camadas. A dependência aponta para dentro e o `ArchitectureTest` (ArchUnit) verifica essa regra.

```text
infrastructure  ->  application  ->  domain

HTTP -> Controller -> Service -> Repository (porta) -> Adapter JPA -> Banco
```

| Camada | Pacotes | Responsabilidade |
| :--- | :--- | :--- |
| `domain` | `model`, `repository`, `port`, `exception` | Modelos e portas, sem Spring e sem JPA |
| `application` | `service`, `command` | Casos de uso. Recebem commands e devolvem objetos de domínio |
| `infrastructure` | `web`, `persistence`, `security`, `config` | Controllers, DTOs, entidades JPA, adapters, BCrypt e OpenAPI |

O fluxo de uma requisição é: o controller valida o DTO, converte para command, o serviço aplica a regra e o adapter persiste. Erros de negócio saem pelo `GlobalExceptionHandler` como ProblemDetail.

Senhas ficam em hash BCrypt. O login compara o hash e não devolve a senha. A troca de senha continua em endpoint separado da atualização cadastral.

### 📂 Estrutura do projeto

```text
src/main/java/com/restaurant/management/
├── domain/
│   ├── model/          # User, UserType, Restaurant, MenuItem, Address
│   ├── repository/     # Portas de persistência
│   ├── port/           # PasswordHasher
│   └── exception/
├── application/
│   ├── command/
│   └── service/
└── infrastructure/
    ├── web/            # Controllers, GlobalExceptionHandler e dto/
    ├── persistence/    # Entidades JPA, Jpa*Repository, adapters e mappers
    ├── security/       # BcryptPasswordHasher
    └── config/         # OpenApiConfig
```

## 🛠️ Tecnologias

* ☕ **Java 21**
* 🍃 **Spring Boot 4.1** (Web, Data JPA, Validation)
* 🐬 **MySQL 8.0** em execução; **H2** nos testes
* 🔐 **Spring Security Crypto** (BCrypt)
* 📘 **Springdoc OpenAPI 3**
* 🧰 **Lombok** e **Jakarta Bean Validation**
* 📊 **JaCoCo** e **ArchUnit** (cobertura mínima de 80% e testes de arquitetura)
* 🐳 **Docker** e **Docker Compose**

🧩 4. Modelagem

O tipo do usuário é a entidade `UserType`, associada ao usuário por `user_type_id`. Não há hierarquia `Client` / `RestaurantOwner`. O endereço do usuário é um objeto embutido.

### Usuário (`users`)

| Campo | Tipo | Observação |
| :--- | :--- | :--- |
| `id` | Long | Chave gerada |
| `name` | String | Obrigatório |
| `email` | String | Obrigatório, único |
| `login` | String | Obrigatório, único |
| `password` | String | Hash BCrypt, 60 caracteres |
| `last_change` | LocalDateTime | Atualizado a cada gravação |
| `user_type_id` | Long | FK para `user_types` |
| endereço | embeddable | `street`, `number`, `city`, `zip_code` |

### Tipo de usuário (`user_types`)

| Campo | Tipo | Observação |
| :--- | :--- | :--- |
| `id` | Long | Chave gerada |
| `name` | String | Obrigatório, único. Campo pedido no enunciado |
| `observation` | String | Opcional |

### Restaurante (`restaurants`)

| Campo | Tipo | Observação |
| :--- | :--- | :--- |
| `id` | Long | Chave gerada |
| `nome` | String | Obrigatório |
| `endereco` | String | Obrigatório |
| `tipo_cozinha` | String | Obrigatório |
| `horario_funcionamento` | String | Obrigatório |
| `owner_id` | Long | FK para `users` |

### Item de cardápio (`menu_items`)

| Campo | Tipo | Observação |
| :--- | :--- | :--- |
| `id` | Long | Chave gerada |
| `nome` | String | Obrigatório |
| `descricao` | String | Obrigatório |
| `preco` | Double | Maior que zero |
| `disponivel_no_local` | Boolean | Consumo apenas no restaurante |
| `foto_path` | String | Caminho da foto. Opcional |
| `restaurant_id` | Long | FK para `restaurants` |

O Hibernate cria e atualiza as tabelas com `ddl-auto: update`.

Bancos criados antes da remoção da herança de usuário ainda têm a coluna `users_type` (`NOT NULL`). O `update` não apaga coluna, e o cadastro de usuário nesses bancos responde 409. Para recriar o volume local: `docker compose down -v`.

## 🔌 5. Endpoints

Base: `http://localhost:8080`. Erros seguem ProblemDetail (RFC 7807).

### 🔑 Autenticação

| Método | Endpoint | Descrição | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Confere login e senha | `200` / `400` |

Não há sessão. A resposta informa se as credenciais são válidas.

```json
{ "login": "joaosilva", "password": "senha123" }
```

```json
{ "valid": true, "message": "Login válido", "userId": 1, "type": "Cliente" }
```

### 🏷️ Tipos de usuário

Campo obrigatório: `name`. `observation` é opcional.

| Método | Endpoint | Descrição | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/user-types` | Cria um tipo | `201` / `400` / `409` |
| `GET` | `/api/v1/user-types` | Lista os tipos | `200` |
| `GET` | `/api/v1/user-types/{id}` | Busca por id | `200` / `404` |
| `PUT` | `/api/v1/user-types/{id}` | Atualiza nome e observação | `200` / `400` / `404` / `409` |
| `DELETE` | `/api/v1/user-types/{id}` | Remove o tipo | `204` / `404` |

```json
{ "name": "Cliente", "observation": "Usuário que consulta o cardápio" }
```

### 👤 Usuários

| Método | Endpoint | Descrição | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/users` | Cadastra usuário ligado a um tipo (`id_tipo_usuario`) | `201` / `400` / `409` |
| `GET` | `/api/v1/users` | Lista usuários. Filtro opcional: `?name=` | `200` |
| `GET` | `/api/v1/users/{id}` | Busca por id | `200` / `404` |
| `PUT` | `/api/v1/users/{id}` | Atualiza nome, e-mail e endereço | `200` / `400` / `404` / `409` |
| `PUT` | `/api/v1/users/{id}/password` | Troca a senha | `204` / `400` / `404` |
| `PUT` | `/api/v1/users/{id}/type/{typeId}` | Associa um tipo existente ao usuário | `200` / `404` |
| `DELETE` | `/api/v1/users/{id}` | Remove o usuário | `204` / `404` |

```json
{
  "name": "João Silva",
  "email": "joao.silva@email.com",
  "login": "joaosilva",
  "password": "senha123",
  "id_tipo_usuario": 1,
  "address": {
    "street": "Rua das Flores",
    "number": "123",
    "city": "São Paulo",
    "zipCode": "01310-100"
  }
}
```

A atualização aceita apenas `name`, `email` e `address`. Senha e tipo têm endpoints próprios.

```json
{ "actualPassword": "senha123", "newPassword": "senhaNova456" }
```

A troca de senha responde `204` sem corpo.

### 🏪 Restaurantes

| Método | Endpoint | Descrição | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/restaurants` | Cria restaurante e vincula o dono (`ownerId`) | `201` / `400` / `404` |
| `GET` | `/api/v1/restaurants` | Lista restaurantes | `200` |
| `GET` | `/api/v1/restaurants/{id}` | Busca por id | `200` / `404` |
| `PUT` | `/api/v1/restaurants/{id}` | Atualiza os dados e o dono | `200` / `400` / `404` |
| `DELETE` | `/api/v1/restaurants/{id}` | Remove o restaurante | `204` / `404` |

```json
{
  "nome": "Sabor do Bairro",
  "endereco": "Rua das Flores, 123",
  "tipoCozinha": "Italiana",
  "horarioFuncionamento": "Segunda a domingo, 18:00-23:00",
  "ownerId": 1
}
```

### 🍕 Itens de cardápio

`fotoPath` é só o caminho. `disponivelNoLocal` indica consumo apenas no restaurante.

| Método | Endpoint | Descrição | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/menu-items` | Cria item ligado a um restaurante (`restaurantId`) | `201` / `400` / `404` |
| `GET` | `/api/v1/menu-items` | Lista os itens | `200` |
| `GET` | `/api/v1/menu-items/{id}` | Busca por id | `200` / `404` |
| `PUT` | `/api/v1/menu-items/{id}` | Atualiza o item | `200` / `400` / `404` |
| `DELETE` | `/api/v1/menu-items/{id}` | Remove o item | `204` / `404` |

```json
{
  "nome": "Pizza Margherita",
  "descricao": "Pizza artesanal com molho de tomate e mussarela",
  "preco": 39.90,
  "disponivelNoLocal": true,
  "fotoPath": "/images/pizza-margherita.jpg",
  "restaurantId": 1
}
```

📮 A collection Postman com esses fluxos está em `docs/postman/restaurant_management.postman_collection`.

### Erro padronizado

E-mail duplicado, nome de tipo duplicado e recurso inexistente saem assim:

```json
{
  "type": "about:blank",
  "title": "Conflict",
  "status": 409,
  "detail": "E-mail já cadastrado: joao.silva@email.com"
}
```

## 📖 6. Swagger

Com a aplicação na porta 8080:

* 👉 **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* 👉 **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

As tags são Autenticação, Usuários, Tipos de Usuário, Restaurantes e Itens do Menu.

## 📮 7. Coleção Postman

A coleção da Fase 2 está em `docs/postman/restaurant_management.postman_collection`.

Ela cobre autenticação, usuários, tipos de usuário (incluindo a associação `PUT /api/v1/users/{id}/type/{typeId}`), restaurantes e itens de cardápio. A variável `baseUrl` aponta para `http://localhost:8080`.

## 🚀 8. Como executar

### ✅ Pré-requisitos

* Docker e Docker Compose, ou
* JDK 21, Maven e um MySQL 8 acessível na porta `3307`.

### 🐳 Docker Compose

Na raiz do repositório:

```bash
git clone https://github.com/rodrfit/restaurant-management.git
cd restaurant-management
cp .env.example .env
docker compose up --build -d
```

O Compose sobe o MySQL 8 em `localhost:3307` e a aplicação em `localhost:8080`. A API só inicia depois que o MySQL fica saudável. O healthcheck usa o usuário `root` com a senha `root`, então `MYSQL_ROOT_PASSWORD` no `.env` precisa ser `root`.

| Variável | Exemplo | Uso |
| :--- | :--- | :--- |
| `MYSQL_ROOT_PASSWORD` | `root` | Senha de root. O healthcheck espera `root` |
| `MYSQL_DATABASE` | `restaurant_management` | Banco criado na primeira subida |
| `MYSQL_USER` | `management` | Usuário da aplicação |
| `MYSQL_PASSWORD` | `management123` | Senha da aplicação |

```bash
docker compose logs -f app
docker compose down
```

`docker compose down -v` apaga o volume do MySQL.

### ☕ Maven, com o MySQL do Compose

```bash
cp .env.example .env
docker compose up -d mysql
./mvnw spring-boot:run
```

A aplicação local usa `jdbc:mysql://localhost:3307/restaurant_management` com o usuário `management` / `management123`, salvo se `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD` estiverem definidas.

## 🧪 9. Testes
### 🧪 Testes e Cobertura (JaCoCo)

Para rodar a suíte de testes e gerar o relatório de cobertura do **JaCoCo**:

```bash
./mvnw clean test
```
Para rodar os testes e também validar a regra de cobertura mínima de **80% de instruções** (`jacoco:check`):

```bash
./mvnw clean verify
```

Os testes usam JUnit 5 e Mockito. O contexto Spring sobe com H2 (`src/test/resources/application.yml`) e não depende do MySQL.

Há testes de serviço (usuário, tipo, restaurante, cardápio e login), de controller, do hash BCrypt, do `GlobalExceptionHandler`, de carga de contexto e o `ArchitectureTest` com ArchUnit.

#### 📊 Como visualizar o relatório do JaCoCo

Após executar `./mvnw clean test` (ou `./mvnw clean verify`), o relatório é gerado no diretório `target/site/jacoco/`:

* **Relatório visual (HTML):** `target/site/jacoco/index.html`
* **Relatório CSV / XML:** `target/site/jacoco/jacoco.csv` e `target/site/jacoco/jacoco.xml`

Para abrir o relatório HTML diretamente no navegador pelo terminal:

* **Linux:**
  ```bash
  xdg-open target/site/jacoco/index.html
  ```
* **macOS:**
  ```bash
  open target/site/jacoco/index.html
  ```
* **Windows (PowerShell / CMD):**
  ```powershell
  start target/site/jacoco/index.html
  ```

## 📖 Documentação da API

## 📦 10. Artefatos

* Código-fonte Spring Boot / Java 21
* `docker-compose.yml`, `Dockerfile` e `.env.example`
* Coleção Postman em `docs/postman/restaurant_management.postman_collection`
* Swagger em tempo de execução
* Relatório da Fase 2: `docs/Tech_Challenge_Fase2_Relatorio.md`
* Este README

## 📚 11. Referências

* [Spring Boot](https://spring.io/projects/spring-boot)
* [RFC 7807 — Problem Details for HTTP APIs](https://datatracker.ietf.org/doc/html/rfc7807)
* [Springdoc OpenAPI](https://springdoc.org/)
* [Docker Compose](https://docs.docker.com/compose/)
* Martin, Robert C. *Clean Architecture*. Prentice Hall, 2017.
