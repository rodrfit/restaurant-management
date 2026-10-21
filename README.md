# 🍽️ Restaurant Management System — FIAP Tech Challenge (Fase 2)

> Pós-Graduação em Arquitetura e Desenvolvimento Java — FIAP / Postech

API REST para um sistema compartilhado de gestão de restaurantes. Nesta fase o serviço passa a cadastrar tipos de usuário, associá-los a usuários existentes, cadastrar restaurantes e os itens de cardápio vendidos por cada estabelecimento.

## 👨‍💻 Autores

* **Marcos Ruiz Freire** (`marcosruizfreire@gmail.com`)
* **José Bernardino da Silva Júnior** (`jbernardino.jr@gmail.com`)
* **Lucas Brisolla** (`lucasgbrisolla@gmail.com`)
* **Rodrigo Fittipaldi** (`rodrfit@gmail.com`)

**Repositório:** [github.com/rodrfit/restaurant-management](https://github.com/rodrfit/restaurant-management)

## 🎯 Problema

Vários restaurantes da região precisavam de um sistema único de gestão, em vez de contratar um produto separado para cada casa. O cliente escolhe o restaurante pela comida. O sistema precisa deixar o dono operar o estabelecimento e o cliente consultar o que está disponível.

A entrega é faseada. A Fase 1 cobriu o cadastro de usuários. A Fase 2 acrescenta:

* 🏷️ **Tipos de usuário:** nome do tipo e associação a um usuário já cadastrado.
* 🏪 **Restaurante:** nome, endereço, tipo de cozinha, horário de funcionamento e dono (um usuário existente).
* 🍕 **Item de cardápio:** nome, descrição, preço, consumo apenas no local e caminho da foto do prato.

## 🏗️ Arquitetura

A aplicação é um monolito Spring Boot organizado em camadas, com a regra de negócio fora dos controllers e o acesso a dados isolado nos repositórios.

| Camada | Pacote | Responsabilidade |
|---|---|---|
| API | `controller`, `dto` | Endpoints REST, validação de entrada e contratos de request/response |
| Aplicação | `service`, `factory` | Casos de uso, associação de tipo de usuário e criação de `Client` / `RestaurantOwner` |
| Persistência | `model`, `repository` | Entidades JPA e Spring Data |
| Infraestrutura de apoio | `config`, `exception` | OpenAPI, hash de senha e erros no formato RFC 7807 (`ProblemDetail`) |

O usuário é uma hierarquia JPA em tabela única (`InheritanceType.SINGLE_TABLE`): `User` é a raiz, `Client` e `RestaurantOwner` são as especializações, discriminadas pela coluna `users_type`. O tipo exibido na API vem da entidade `UserType` (`user_types`), associada por `user_type_id`. O nome do tipo também define qual subclasse concreta a `UserFactory` instancia (cliente ou dono).

Senhas são armazenadas como hash BCrypt. O login compara o hash e não devolve a senha.

### 📂 Estrutura do projeto

```text
src/main/java/com/restaurant/management/
├── config/          # OpenAPI e PasswordEncoder
├── controller/      # Auth, User, UserType, Restaurant, MenuItem
├── dto/
│   ├── request/
│   └── response/
├── enums/
├── exception/
├── factory/         # UserFactory
├── model/           # User, Client, RestaurantOwner, UserType, Address, Restaurant, MenuItem
├── repository/
└── service/
    └── impl/
```

## 🛠️ Tecnologias

* ☕ **Java 21**
* 🍃 **Spring Boot 4.1** (Web, Data JPA, Validation)
* 🐬 **MySQL 8.0** em execução; **H2** nos testes
* 🔐 **Spring Security Crypto** (BCrypt)
* 📘 **Springdoc OpenAPI 3**
* 🧰 **Lombok** e **Jakarta Bean Validation**
* 🐳 **Docker** e **Docker Compose**

## 🔌 Endpoints

Base: `http://localhost:8080`

Erros de validação, conflito e recurso inexistente de usuário ou tipo de usuário saem como `ProblemDetail` (RFC 7807).

### 🔑 Autenticação

| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | Confere login e senha | `200` / `400` |

O corpo da resposta indica se as credenciais são válidas (`valid`), sem gravar sessão.

### 🏷️ Tipos de usuário

Campo obrigatório: `name`. `observation` é opcional.

| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| `POST` | `/api/v1/user-types` | Cria um tipo | `201` / `400` / `409` |
| `GET` | `/api/v1/user-types` | Lista os tipos | `200` |
| `GET` | `/api/v1/user-types/{id}` | Busca por id | `200` / `404` |
| `PUT` | `/api/v1/user-types/{id}` | Atualiza nome e observação | `200` / `400` / `404` / `409` |
| `DELETE` | `/api/v1/user-types/{id}` | Remove o tipo | `204` / `404` |

### 👤 Usuários

| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| `POST` | `/api/v1/users` | Cadastra usuário já ligado a um tipo (`id_tipo_usuario`) | `201` / `400` / `409` |
| `GET` | `/api/v1/users` | Lista usuários. Filtro opcional: `?name=` | `200` |
| `GET` | `/api/v1/users/{id}` | Busca por id | `200` / `404` |
| `PUT` | `/api/v1/users/{id}` | Atualiza nome, e-mail e endereço | `200` / `400` / `404` / `409` |
| `PUT` | `/api/v1/users/{id}/password` | Troca a senha | `204` / `400` / `404` |
| `PUT` | `/api/v1/users/{id}/type/{typeId}` | Associa um tipo existente ao usuário | `200` / `404` |
| `DELETE` | `/api/v1/users/{id}` | Remove o usuário | `204` / `404` |

Exemplo de cadastro:

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

A atualização (`PUT /api/v1/users/{id}`) aceita apenas `name`, `email` e `address`. Senha e tipo têm endpoints próprios.

### 🏪 Restaurantes

| Método | Endpoint | Descrição | Status |
|---|---|---|---|
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

`fotoPath` é o caminho onde a foto ficaria. O backend não armazena o arquivo. `disponivelNoLocal` indica consumo apenas no restaurante.

| Método | Endpoint | Descrição | Status |
|---|---|---|---|
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

📮 A collection Postman com esses fluxos está em `docs/postman/RestaurantManagement.fase2.2.postman_collection.json`.

## 🚀 Como executar

### ✅ Pré-requisitos

* Docker e Docker Compose, ou
* JDK 21, Maven e um MySQL 8 acessível na porta `3307`.

### 🐳 Docker Compose

Na raiz do repositório:

```bash
cp .env.example .env
docker compose up --build -d
```

O Compose sobe o MySQL 8 (`localhost:3307`) e a aplicação (`localhost:8080`). A API só inicia depois que o MySQL responde ao healthcheck com a senha definida em `MYSQL_ROOT_PASSWORD`.

Variáveis em `.env.example` (os mesmos valores padrão do `application.yml` para execução local):

| Variável | Exemplo | Uso |
|---|---|---|
| `MYSQL_ROOT_PASSWORD` | `root` | Senha de root e healthcheck |
| `MYSQL_DATABASE` | `restaurant_management` | Banco criado na primeira subida |
| `MYSQL_USER` | `management` | Usuário da aplicação |
| `MYSQL_PASSWORD` | `management123` | Senha da aplicação |

Acompanhar a subida:

```bash
docker compose logs -f app
```

Encerrar:

```bash
docker compose down
```

`docker compose down -v` apaga também o volume do MySQL.

### ☕ Maven, com o MySQL do Compose

```bash
cp .env.example .env
docker compose up -d mysql
./mvnw spring-boot:run
```

A aplicação local usa `jdbc:mysql://localhost:3307/restaurant_management` com o usuário `management` / `management123`, salvo se `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD` estiverem definidas.

### 🧪 Testes

```bash
./mvnw test
```

Os testes usam H2 em memória (`src/test/resources/application.yml`) e não dependem do MySQL.

## 📖 Documentação da API

Com a aplicação no ar:

* 👉 **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* 👉 **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## 📚 Referências

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Springdoc OpenAPI](https://springdoc.org/)
* [Docker Compose](https://docs.docker.com/compose/)
