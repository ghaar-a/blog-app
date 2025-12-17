Blog Pessoal API
API RESTful de um blog pessoal desenvolvido em Java com Spring Boot. Funcionalidades completas de posts, comentários, tags, autenticação e autorização.
Funcionalidades

Cadastro e login de usuários com autenticação JWT (stateless)
CRUD de posts restrito ao autor ou administrador
Sistema de tags com relacionamento many-to-many
Comentários com suporte a replies (estrutura hierárquica)
Listagem de posts com paginação, filtros por tag/autor e busca por título/conteúdo
Controle de acesso baseado em roles (USER e ADMIN)
Validações de entrada e tratamento global de exceções
Documentação da API com Swagger/OpenAPI

Tecnologias

Java 17
Spring Boot 3.2
Spring Data JPA (Hibernate)
Spring Security + JJWT
PostgreSQL
Lombok
Maven
Springdoc OpenAPI

Estrutura do projeto
textsrc/main/java/com/example/blog
├── controller     → Endpoints REST
├── dto            → Objetos de transferência (request/response)
├── exception      → Tratamento centralizado de erros
├── model          → Entidades JPA
├── repository     → Interfaces JPA com queries customizadas
├── security       → Configuração JWT, filtro e SecurityConfig
└── service        → Regras de negócio e mapeamentos
Como executar
Requisitos

Java 17
Maven
PostgreSQL (porta 5432)

Passos

Clone o repositórioBashgit clone https://github.com/seuusuario/blog-pessoal-api.git
cd blog-pessoal-api
Crie o banco de dadosSQLCREATE DATABASE blogdb;
Configure as credenciais em src/main/resources/application.propertiespropertiesspring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
jwt.secret=sua_chave_secreta_forte
Inicie a aplicaçãoBashmvn spring-boot:run

A API ficará disponível em http://localhost:8080.
A documentação interativa pode ser acessada em http://localhost:8080/swagger-ui.html.
Exemplos de requisições
Registro
Bashcurl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"usuario","email":"user@email.com","password":"senha123"}'
Login
Bashcurl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"usuario","password":"senha123"}'
Criar post (com token)
Bashcurl -X POST http://localhost:8080/api/posts \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Título do post",
    "content": "Conteúdo completo aqui",
    "tags": ["java", "spring-boot"]
  }'
Listar posts com filtro
textGET /api/posts?page=0&size=10&tag=java
Melhorias futuras

Renderização de Markdown no conteúdo
Upload de imagens
Sistema de likes e favoritos
Testes de integração com Testcontainers
Deploy em ambiente cloud
