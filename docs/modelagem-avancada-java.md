# Modelagem avancada Java

Este projeto mantem o DDL Oracle como fonte de verdade e usa `spring.jpa.hibernate.ddl-auto=validate`.
As evidencias abaixo foram adicionadas sem alterar tabelas, colunas, endpoints ou regras de negocio.

## Heranca com `@MappedSuperclass`

A classe `EntidadeAuditavel`, em `br.com.fiap.amanaje.common.model`, concentra os campos operacionais compartilhados por entidades que possuem exatamente as mesmas colunas de auditoria no DDL:

- `ST_ATIVO`
- `DT_CRIADO_EM`
- `DT_ATUALIZADO_EM`
- `DT_DEL_EM`
- `ID_DEL_POR`
- `DS_MOTIVO_EXCLUSAO`

Entidades que herdam esse modelo nos subpacotes `model` dos respectivos dominios:

- `Cliente`
- `Usuario`
- `RegiaoMonitorada`
- `EstacaoIot`
- `Alerta`

O mapeamento de `ST_ATIVO` preserva compatibilidade com Oracle `CHAR(1)` usando `columnDefinition = "CHAR(1)"` e `@JdbcTypeCode(SqlTypes.CHAR)`.

## Embedded com `@Embeddable`

A classe `DadosRedeEstacao`, em `br.com.fiap.amanaje.common.model`, agrupa dados opcionais de rede de `br.com.fiap.amanaje.estacoes.model.LogStatusEstacao`:

- `DS_IP_ADDRESS`
- `DS_VERSAO_FIRMWARE`

`LogStatusEstacao` usa `@Embedded` com `@AttributeOverrides` para manter os nomes de colunas definidos no DDL Oracle.

## Multiplas tabelas com JPA

O MVP mapeia 13 tabelas `TB_AMANAJE_*` por entidades JPA e repositories Spring Data:

- `TB_AMANAJE_CLI`
- `TB_AMANAJE_USU`
- `TB_AMANAJE_REGIAO_MONIT`
- `TB_AMANAJE_EST_IOT`
- `TB_AMANAJE_LEIT_IOT`
- `TB_AMANAJE_OBS_CLIM`
- `TB_AMANAJE_AVAL_RISCO`
- `TB_AMANAJE_ALERTA`
- `TB_AMANAJE_IND_REG`
- `TB_AMANAJE_HIST_EVENTO`
- `TB_AMANAJE_LOG_STATUS_EST`
- `TB_AMANAJE_PROCESS`
- `TB_AMANAJE_LOG_ERRO`

Cada tabela operacional possui um `JpaRepository` correspondente para leitura e persistencia.

## Consistencia composta no Oracle

O DDL usa chaves estrangeiras compostas e restricoes unicas para manter consistencia regional entre tabelas. Exemplos:

- `UQ_AMANAJE_EST_ID_REGIAO` e `FK_LEIT_EST_REGIAO` garantem que uma leitura IoT pertence a uma estacao da mesma regiao.
- `UQ_AMANAJE_LEIT_ID_REGIAO` e `FK_AVAL_LEIT_REGIAO` mantem a avaliacao de risco ligada a leitura da mesma regiao.
- `FK_AVAL_OBS_REGIAO` e `FK_ALERTA_AVAL_REGIAO` preservam a consistencia regional para observacoes, avaliacoes e alertas.

No Java, esses relacionamentos foram mapeados intencionalmente como IDs escalares (`Long`). A validacao de repositorio e servico confirma os vinculos necessarios para o MVP, enquanto o Oracle continua aplicando as restricoes compostas reais.

## Por que `@EmbeddedId` nao foi forcado

O schema nao define chaves primarias compostas. Forcar `@EmbeddedId` criaria complexidade artificial e poderia aumentar o risco de divergencia entre Java e DDL. A decisao foi preservar IDs simples no modelo Java e documentar que a consistencia composta pertence ao desenho relacional do Oracle.
