# 🍽️ Restaurant Management System - FIAP Tech Challenge (Fase 1)

> **Pós-Graduação em Arquitetura e Desenvolvimento Java — FIAP / Postech**

---

## 👨‍💻 Autores
* **Marcos Ruiz Freire** (`marcosruizfreire@gmail.com`)
* **José Bernardino da Silva Júnior** (`jbernardino.jr@gmail.com`)
* **Lucas Brisolla** (`lucasgbrisolla@gmail.com`)
* **Rodrigo Fittipaldi** (`rodrfit@gmail.com`)

**Data:** Agosto / 2026  
**Repositório:** [GitHub - restaurant-management](https://github.com/marcosruizmrf/restaurant-management)

---

## 📜 1. Introdução
Este projeto consiste no desenvolvimento da primeira fase da aplicação **Restaurant Management System**, desenvolvida como requisito parcial para a conclusão da Fase 1 da Pós-Graduação em Arquitetura e Desenvolvimento Java da **FIAP (Postech)**. O objetivo principal é estruturar um serviço backend escalável, resiliente e bem arquitetado para solucionar gargalos operacionais no gerenciamento de restaurantes, cadastro de usuários e pedidos de clientes.

---

## 🎯 2. Definição do Problema
O setor gastronômico e de gestão de restaurantes enfrenta desafios diários, como:
* **Lentidão no Atendimento e Cadastro:** Dificuldade na identificação e gestão de diferentes perfis de usuários (Clientes vs. Donos de Restaurantes).
* **Inconsistência em Dados de Perfil e Endereço:** Falta de padronização nas informações cadastrais dos usuários e seus respectivos endereços, impactando entregas e atendimento.
* **Erros Operacionais e Gargalos de Integração:** Ausência de uma API REST padronizada, documentada e resiliente para suportar a escala do ecossistema.

A solução proposta aborda esses problemas entregando um serviço RESTful centralizado com persistência relacional, validação rigorosa de dados de entrada e uma arquitetura orientada ao **Domain-Driven Design (DDD)**.

---

## 📋 3. Levantamento de Requisitos

### 3.1 Event Storming
Através de um workshop de **Event Storming**, identificamos os principais eventos de domínio, comandos e agregados ao longo do ciclo de vida da aplicação:
* **Eventos de Domínio:** `UsuarioCadastrado`, `DadosUsuarioAtualizados`, `EnderecoAtualizado`.
* **Comandos:** `CadastrarUsuario`, `AtualizarUsuario`.
* **Atores / Perfis:** Cliente (`CLIENT`) e Dono de Restaurante (`RESTAURANT_OWNER`).

### 3.2 Mapeamento de Demandas e Necessidades
* **Perfis de Usuários:**
  * **Cliente (`CLIENT`):** Requer cadastro simplificado com endereço para acompanhamento de pedidos e relatórios.
  * **Dono de Restaurante (`RESTAURANT_OWNER`):** Requer cadastro completo e permissões administrativas para gerenciar restaurantes.
* **Regras de Negócio:** Unicidade de e-mail e login por usuário, rastreabilidade de atualizações (`lastChange`) e validação estrita de endereços.

### 3.3 Agregados de Domínio
* **Agregado `User` (Raiz de Agregação / Aggregate Root):**
  * **Entidades:** `User` (raiz abstrata), `Client`, `RestaurantOwner`.
  * **Value Objects (Objetos de Valor):** `Address` (Embedded no modelo relacional).
  * **Enumerações:** `UserType` (`CLIENT`, `RESTAURANT_OWNER`).

---

## 🏗️ 4. Arquitetura do Sistema

### 4.1 Abordagem Domain-Driven Design (DDD)
A arquitetura segue os princípios de DDD e Clean Architecture, promovendo alta coesão e baixo acoplamento:
* **Separation of Concerns (SoC):** Separação clara entre Controllers (Camada de API), Services (Regras de Negócio), Factories (Criação de Objetos), Repositories (Acesso a Dados) e DTOs.
* **Polimorfismo via JPA Single Table:** Utilização de `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)` na classe `User` para armazenar `Client` e `RestaurantOwner` na mesma tabela (`users`), diferenciados pela coluna discriminadora `dtype`.

### 4.2 Padrões de Projeto Aplicados (SOLID)
* **Single Responsibility Principle (SRP):**
  * As regras de negócio e validações residem na camada de serviço (`UserServiceImpl`).
  * A responsabilidade de criação polimórfica das entidades foi isolada no componente **`UserFactory`**.
* **Open/Closed Principle (OCP):** Novos perfis de usuários podem ser adicionados ao `UserFactory` sem modificar o código de serviço existente.
* **Factory Pattern (`UserFactory`):** Encapsula a lógica de instanciação de entidades concretas (`Client` ou `RestaurantOwner`) e transformações do Objeto de Valor `Address`.

---

## 🧱 Arquitetura

O projeto segue **Clean Architecture** de forma pragmática, em 3 camadas. A regra central é que as dependências apontam sempre para dentro, em direção ao domínio.

```text
┌──────────────────────────────────────────────────────────────────┐
│ infrastructure                                                   │
│   web · persistence · security · config                          │
│                                                                  │
│   ┌──────────────────────────────────────────────────────────┐   │
│   │ application                                              │   │
│   │   service (interface + impl) · command                   │   │
│   │                                                          │   │
│   │   ┌──────────────────────────────────────────────────┐   │   │
│   │   │ domain                                           │   │   │
│   │   │   model · repository (interfaces) · port ·       │   │   │
│   │   │   exception                                      │   │   │
│   │   └──────────────────────────────────────────────────┘   │   │
│   └──────────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────────┘

Fluxo de uma requisição:

HTTP -> Controller -> Service -> Repository (porta) -> Adapter JPA -> Banco
        (Request ->   (regras    (interface em         (Entity <->
         Command,      de         domain.repository)    Domain via
         Domain ->     negócio)                         Mapper)
         Response)
```

### Regra de dependência

* `domain` não depende de nenhuma outra camada nem de frameworks (sem `org.springframework`, `jakarta.persistence`, Jackson ou Swagger). Lombok é permitido.
* `application` depende apenas de `domain`. Usa `@Service` e `@Transactional` do Spring (concessão pragmática), mas não importa nada de `infrastructure` nem tipos web (DTOs, `ResponseEntity`).
* `infrastructure` pode depender de `domain` e `application`.

A regra é verificada automaticamente pelo teste `ArchitectureTest` (ArchUnit), que roda junto com `./mvnw verify`.

### Pacotes e responsabilidades

Base: `com.restaurant.management`

| Pacote | Responsabilidade |
| :--- | :--- |
| `domain.model` | Modelos de domínio puros (`User`, `UserType`, `Restaurant`, `MenuItem`, `Address`), incluindo a regra de `lastChange` |
| `domain.repository` | Interfaces (portas) de acesso a dados, com apenas os métodos realmente usados |
| `domain.port` | Portas para capacidades externas (`PasswordHasher`) |
| `domain.exception` | Exceções de negócio (`*NotFoundException`, `EmailAlreadyExistsException` etc.) e `ExceptionMessages` |
| `application.command` | Records de entrada dos services (`CreateUserCommand`, `SaveRestaurantCommand` etc.), independentes do formato HTTP |
| `application.service` | Interfaces dos casos de uso e implementações em `impl`; orquestram regras de negócio e transações, recebem commands e devolvem objetos de domínio |
| `infrastructure.web` | Controllers `*ControllerV1`, DTOs de request/response e `GlobalExceptionHandler`; convertem Request -> Command e domínio -> Response |
| `infrastructure.persistence` | Entidades JPA, interfaces Spring Data, adapters que implementam `domain.repository` e mappers entidade <-> domínio |
| `infrastructure.security` | `BcryptPasswordHasher`, implementação de `PasswordHasher` (strength 12) |
| `infrastructure.config` | Configurações de framework (`OpenApiConfig`) |

### Por que manter service interface + impl

* O controller depende da interface, o que permite testá-lo isolado com um mock do service.
* A interface define o contrato da camada `application` (commands de entrada, objetos de domínio de saída) e a implementação pode mudar sem afetar quem consome.
* Mantém o projeto pragmático: não há um UseCase por operação, apenas um service por agregado.

---

## 🛠️ 5. Tecnologias Utilizadas

* **Linguagem:** Java 21 LTS (`<java.version>21</java.version>`)
* **Framework Backend:** Spring Boot 4.1.0 (`spring-boot-starter-parent` `4.1.0`)
* **Persistência:** Spring Data JPA / Hibernate 6
* **Segurança e Criptografia:** Spring Security Crypto (`spring-security-crypto`)
* **Banco de Dados:** MySQL 8.0 (Runtime), H2 Database (Escopo de Testes)
* **Documentação OpenAPI:** Springdoc OpenAPI 3.1.0 (`springdoc-openapi-starter-webmvc-ui` `3.1.0` / Swagger UI)
* **Utilitários e Validação:** Lombok 1.18.46 (`<lombok.version>1.18.46</lombok.version>`), Jakarta Bean Validation
* **Containers e Ambiente:** Docker e Docker Compose

---

## 📂 6. Estrutura do Projeto

```text
src/main/java/com/restaurant/management/
├── controller/            # Endpoints da API REST (UserControllerV1)
├── dto/
│   ├── request/           # DTOs de entrada (CreateUserRequest, UpdateUserRequest)
│   └── response/          # DTOs de saída (UserResponse, AddressResponse)
├── enums/                 # Enums de domínio (UserType)
├── exception/             # Manipulação global de exceções (GlobalExceptionHandler)
├── factory/               # Factory Pattern para instanciação de entidades (UserFactory)
├── model/                 # Entidades JPA de Domínio (User, Client, RestaurantOwner, Address)
├── repository/            # Repositórios Spring Data JPA (UserRepository)
└── service/               # Camada de Serviços (UserService, UserServiceImpl)
```

---

## 🔌 7. Endpoints da API REST

### 🔑 Autenticação (`AuthControllerV1`)

| Método | Endpoint | Descrição | Status HTTP |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Valida credenciais de login e senha no banco de dados | `200 OK` / `400 Bad Request` |

### 👤 Usuários (`UserControllerV1`)

| Método | Endpoint | Descrição | Status HTTP |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/users` | Cadastro polimórfico de novo usuário (`CLIENT` ou `RESTAURANT_OWNER`) | `201 Created` / `400` / `409` |
| `GET` | `/api/v1/users` | Lista usuários cadastrados (com filtro opcional por nome `?name=...`) | `200 OK` |
| `GET` | `/api/v1/users/{id}` | Busca os detalhes de um usuário específico por ID | `200 OK` / `404 Not Found` |
| `PUT` | `/api/v1/users/{id}` | Atualização de dados cadastrais e endereço de um usuário | `200 OK` / `400` / `404` / `409` |
| `PUT` | `/api/v1/users/{id}/password` | Alterar senha do usuário em endpoint exclusivo | `204 No Content` / `400` / `404` |
| `DELETE` | `/api/v1/users/{id}` | Deleta um usuário do sistema a partir do ID | `204 No Content` / `404 Not Found` |

### Tratamento Global de Exceções (`GlobalExceptionHandler`)
A aplicação utiliza `@RestControllerAdvice(basePackages = "com.restaurant.management")` para padronizar respostas de erro no formato RFC 7807:
* **Conflito (`409 Conflict`):** E-mail ou login duplicado (`EmailAlreadyExistsException` / `DataIntegrityViolationException`).
* **Não Encontrado (`404 Not Found`):** ID de usuário inexistente (`UserNotFoundException`).
* **Erro de Validação (`400 Bad Request`):** Campos inválidos na requisição (`MethodArgumentNotValidException`).

---

## 🐳 8. Como Executar o Projeto

### Pré-requisitos
* **Docker** e **Docker Compose** instalados (recomendado), **ou**
* **JDK 21** e **Maven** instalados localmente.

---

### Opção A: Execução via Docker Compose (Recomendado)

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/marcosruizmrf/restaurant-management.git
   cd restaurant-management
   ```

2. **Subir os containers (Aplicação + Banco de Dados MySQL):**
   ```bash
   docker compose -f src/docker-compose.yml up --build -d
   ```

3. **Verificar os logs da aplicação:**
   ```bash
   docker compose -f src/docker-compose.yml logs -f app
   ```

---

### Opção B: Execução Local via Maven Wrapper

1. **Garantir que o banco de dados MySQL está rodando na porta 3306:**
   *(Você pode subir apenas o container do MySQL via Docker)*:
   ```bash
   docker compose -f src/docker-compose.yml up -d mysql
   ```

2. **Compilar e rodar a aplicação Spring Boot:**
   ```bash
   ./mvnw clean spring-boot:run
   ```

---

## 📖 9. Documentação Swagger / OpenAPI UI

Com a aplicação em execução, acesse a documentação interativa da API no seu navegador:

👉 **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
👉 **Especificação OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🏁 10. Considerações Finais
A aplicação foi projetada focando em resiliência, manutenibilidade e padrões arquiteturais limpos recomendados pela **FIAP**. O levantamento de requisitos com Event Storming e a modelagem de domínio com DDD permitiram um alinhamento perfeito entre as regras de negócio de gestão de restaurantes e a implementação de software moderna em Java 21.

---

## 📚 11. Referências
* **Spring Boot Documentation:** [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
* **Domain-Driven Design (Eric Evans):** Tackling Complexity in the Heart of Software.
* **Springdoc OpenAPI Documentation:** [https://springdoc.org/](https://springdoc.org/)
