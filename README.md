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

REST API demonstrável para clientes, regiões monitoradas, estações IoT, telemetria, observações climáticas, avaliação de risco, alertas, dashboard e indicadores regionais.

As entidades JPA estão alinhadas ao DDL Oracle, que permanece como fonte de verdade do banco de dados. Os relacionamentos regionais são validados pela API com IDs simples nesta fase.

## Modelagem avançada Java

Evidências de modelagem Java Advanced foram adicionadas sem alterar contratos HTTP, regras de negócio ou o DDL Oracle:

- Herança: `EntidadeAuditavel` usa `@MappedSuperclass` para centralizar os campos operacionais compartilhados por `Cliente`, `Usuario`, `RegiaoMonitorada`, `EstacaoIot` e `Alerta`.
- Embedded: `DadosRedeEstacao` usa `@Embeddable` e é incorporado em `LogStatusEstacao` para agrupar dados opcionais de rede.
- Chaves compostas e consistência composta: o Oracle DDL aplica restrições compostas reais, como vínculos por `ID_REGIAO`; no Java, os relacionamentos seguem como IDs escalares para manter o MVP simples e confiável.
- Múltiplas tabelas: o backend mapeia 13 entidades `TB_AMANAJE_*` com repositories Spring Data JPA.

Detalhes adicionais estão em `docs/modelagem-avancada-java.md`.

## Variáveis de ambiente

Configure as seguintes variáveis antes de executar a aplicação:

| Variável | Obrigatória | Descrição |
| --- | --- | --- |
| `SERVER_PORT` | Não | Porta HTTP. O valor padrão é `8080`. |
| `DB_URL` | Recomendado | URL JDBC do banco Oracle. |
| `DB_USERNAME` | Recomendado | Usuário do banco Oracle. |
| `DB_PASSWORD` | Recomendado | Senha do banco Oracle. |

A aplicação usa somente Oracle e valida o schema existente sem criar ou atualizar tabelas automaticamente. O DDL deve ser aplicado separadamente antes da execução da API.

Para produção ou uso real, configure `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` por variáveis de ambiente. O `application.yml` inclui valores fallback da conexão Oracle FIAP apenas para facilitar a execução escolar e demonstrativa.

## Validação Oracle FIAP

A aplicação está configurada com `spring.jpa.hibernate.ddl-auto=validate` para validar o schema no Oracle FIAP. O DDL deve ser aplicado no schema configurado antes de iniciar a API. As variáveis de ambiente podem substituir a conexão padrão da sala de aula.

Se a inicialização falhar com erros de tabela ou coluna ausente, o DDL não foi aplicado corretamente ou algum mapeamento da entidade não corresponde ao banco de dados.

Nenhum dado inicial é incluído automaticamente. A API não executa carga de dados, seed ou inicialização por `CommandLineRunner`.

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

### Usuários

- `POST /api/usuarios`
- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `PUT /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

Filtro opcional em `GET /api/usuarios`: `idCliente`.

O CRUD de usuários é uma administração básica do MVP. Ele não implementa login, JWT ou autenticação. O campo `senhaHash` é aceito para compatibilidade de persistência, mas não é retornado nas respostas.

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

### Riscos

- `POST /api/riscos/avaliar/{idRegiao}`
- `GET /api/regioes/{id}/risco-atual`

A avaliação de risco usa a leitura IoT válida mais recente e a observação climática mais recente disponíveis para a região.

### Alertas

- `GET /api/alertas`
- `PUT /api/alertas/{id}/resolver`

Filtros opcionais em `GET /api/alertas`: `idRegiao`, `status` e `nivel`.

### Dashboard

- `GET /api/dashboard/summary`

Filtro opcional em `GET /api/dashboard/summary`: `idCliente`.

O resumo do dashboard agrega os dados operacionais atuais persistidos no backend.

### Indicadores regionais

- `GET /api/indicadores-regionais`

Filtros opcionais em `GET /api/indicadores-regionais`: `estado`, `cidade`, `tipoRisco` e `nivelRiscoMedio`.

Os indicadores regionais são retornados a partir dos registros persistidos e podem ser preenchidos posteriormente por DML, PL/SQL ou pelo fluxo da aplicação.

## Fluxo de demonstração

Após aplicar o DDL e configurar as variáveis de ambiente do Oracle, use o Swagger UI em http://localhost:8080/swagger-ui/index.html para executar o fluxo:

1. `POST /api/clientes`
2. `POST /api/regioes`
3. `POST /api/estacoes`
4. `POST /api/leituras`
5. `POST /api/observacoes-climaticas`
6. `POST /api/riscos/avaliar/{idRegiao}`
7. `GET /api/regioes/{id}/risco-atual`
8. `GET /api/alertas`
9. `PUT /api/alertas/{id}/resolver`
10. `GET /api/dashboard/summary`
11. `GET /api/indicadores-regionais`

O endpoint de saúde está disponível em http://localhost:8080/api/health. As consultas por ID de clientes, regiões e estações incluem links HATEOAS mínimos para navegação entre recursos relacionados.

## Próximas fases

Integrações adicionais serão implementadas nas próximas fases.
