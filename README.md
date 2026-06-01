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

REST API demonstrável para clientes, regiões monitoradas, estações IoT, telemetria e observações climáticas.

As entidades JPA estão alinhadas ao DDL Oracle, que permanece como fonte de verdade do banco de dados. Os relacionamentos regionais são validados pela API com IDs simples nesta fase.

## Variáveis de ambiente

Configure as seguintes variáveis antes de executar a aplicação:

| Variável | Obrigatória | Descrição |
| --- | --- | --- |
| `SERVER_PORT` | Não | Porta HTTP. O valor padrão é `8080`. |
| `DB_URL` | Sim | URL JDBC do banco Oracle. |
| `DB_USERNAME` | Sim | Usuário do banco Oracle. |
| `DB_PASSWORD` | Sim | Senha do banco Oracle. |

A aplicação usa Oracle e não cria nem atualiza o schema automaticamente. O DDL deve ser aplicado separadamente. A execução local da aplicação requer uma configuração Oracle válida.

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

### Clientes

- `POST /api/clientes`
- `GET /api/clientes`
- `GET /api/clientes/{id}`
- `PUT /api/clientes/{id}`
- `DELETE /api/clientes/{id}`

### Regiões monitoradas

- `POST /api/regioes`
- `GET /api/regioes`
- `GET /api/regioes/{id}`
- `PUT /api/regioes/{id}`
- `DELETE /api/regioes/{id}`

Filtros opcionais em `GET /api/regioes`: `idCliente`, `estado`, `cidade` e `visibilidade`.

### Estações IoT

- `POST /api/estacoes`
- `GET /api/estacoes/regiao/{idRegiao}`
- `GET /api/estacoes/{id}`
- `PUT /api/estacoes/{id}`
- `DELETE /api/estacoes/{id}`

### Leituras IoT

- `POST /api/leituras`
- `GET /api/regioes/{id}/leituras`

`POST /api/leituras` é o endpoint HTTP de contingência e entrada de telemetria para ESP32/Wokwi.

### Observações climáticas

- `POST /api/observacoes-climaticas`
- `GET /api/regioes/{id}/observacoes-climaticas/ultima`

`POST /api/observacoes-climaticas` é o endpoint de integração esperado para o serviço climático em C#.

## Próximas fases

Cálculo de risco, geração de alertas e dashboard serão implementados nas próximas fases.
