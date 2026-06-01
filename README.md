# Amanajé API

API principal do projeto Amanajé Global Solution 2026/1 para monitoramento climático e ambiental de áreas vulneráveis.

## Tecnologias

- Java 17
- Maven
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Oracle Database
- Bean Validation
- Lombok
- Spring HATEOAS
- springdoc-openapi / Swagger UI

## Fase atual

Phase 01 - Spring Boot foundation.

As entidades e os mapeamentos JPA serão implementados e alinhados após o fornecimento do DDL Oracle final, que será a fonte de verdade do banco de dados.

## Variáveis de ambiente

Configure as seguintes variáveis antes de executar a aplicação:

| Variável | Obrigatória | Descrição |
| --- | --- | --- |
| `SERVER_PORT` | Não | Porta HTTP. O valor padrão é `8080`. |
| `DB_URL` | Sim | URL JDBC do banco Oracle. |
| `DB_USERNAME` | Sim | Usuário do banco Oracle. |
| `DB_PASSWORD` | Sim | Senha do banco Oracle. |

A aplicação usa Oracle e não cria nem atualiza o schema automaticamente. O DDL será aplicado separadamente em uma fase posterior. A execução local da aplicação requer uma configuração Oracle válida.

## Executar testes

```bash
./mvnw clean test
```

No Windows:

```powershell
.\mvnw.cmd clean test
```

## Executar localmente

Após configurar o acesso ao Oracle:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Endpoints úteis

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Health check: http://localhost:8080/api/health
