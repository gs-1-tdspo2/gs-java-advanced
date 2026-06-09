# Swagger Demo Flow — Amanajé API

## Antes de gravar

* Acorde o Render primeiro:
  https://gs-java-advanced.onrender.com/api/health
* Abra o Swagger:
  https://gs-java-advanced.onrender.com/swagger-ui/index.html
* Rode os testes localmente antes da gravacao:
  .\mvnw.cmd clean test
* Nao rode a API Java local com MQTT habilitado enquanto o Render tambem estiver com MQTT habilitado. Isso evita processamento duplicado.
* Use a estacao:
  AMANAJE-SP-RP-001
* Use a regiao:
  8
* O plano gratuito do Render pode levar alguns minutos para acordar.

## Demo order — essential

1. Show tests passing:
   .\mvnw.cmd clean test

2. Open:
   GET /api/health

3. Show Oracle-backed data:
   GET /api/clientes
   GET /api/regioes
   GET /api/estacoes/regiao/8

4. Show IoT history:
   GET /api/regioes/8/leituras

5. Create one dangerous IoT reading:
   POST /api/leituras

6. Evaluate risk:
   POST /api/riscos/avaliar/8

7. Show risk/alert result:
   GET /api/regioes/8/risco-atual
   GET /api/alertas?idRegiao=8

8. Show dashboard endpoint:
   GET /api/dashboard/summary

9. Show MQTT evidence:
   Wokwi receives message on:
   app/estacoes/AMANAJE-SP-RP-001/alertas

## Swagger payloads

### POST /api/leituras

Use os nomes HTTP do DTO LeituraIotCreateRequest:

```json
{
  "codigoEstacao": "AMANAJE-SP-RP-001",
  "dtLeitura": "2026-06-09T10:00:00",
  "distanciaAguaCm": 10,
  "nivelAguaPercentual": 95,
  "inclinacaoGraus": 35.00,
  "vibracao": 0.90,
  "pressaoHpa": 940.00,
  "pm25": 180.00,
  "pm10": 300.00
}
```

Esse payload e valido para o DTO atual. A leitura tambem poderia usar aliases MQTT como stationCode e timestamp, mas no Swagger a demonstracao fica mais clara com codigoEstacao e dtLeitura.

## What each endpoint proves

### GET /api/health

No Swagger, abra Health e execute GET /api/health. O retorno esperado e um JSON com status UP. Isso prova que a API esta publicada e respondendo no Render.

### GET /api/clientes

Abra Clientes e execute GET /api/clientes. O retorno esperado e uma lista de clientes ativos. Isso prova que a API consulta dados persistidos no Oracle.

### GET /api/regioes

Abra Regioes Monitoradas e execute GET /api/regioes. O retorno esperado e uma lista de regioes com cidade, estado, coordenadas, tipo de area e nivel de vulnerabilidade. Isso mostra a base territorial monitorada pelo Amanaje.

### GET /api/estacoes/regiao/8

Abra Estacoes IoT e execute GET /api/estacoes/regiao/8. O retorno esperado e a lista de estacoes ativas da regiao 8, incluindo AMANAJE-SP-RP-001 se ela estiver ativa. Isso prova o vinculo entre regiao e dispositivo IoT.

### GET /api/regioes/8/leituras

Abra Leituras IoT e execute GET /api/regioes/8/leituras. O retorno esperado e o historico de leituras da regiao 8. Isso prova que a telemetria recebida por HTTP ou MQTT esta sendo persistida.

### POST /api/leituras

Abra Leituras IoT e execute POST /api/leituras com o payload perigoso. O retorno esperado e HTTP 201 com a leitura criada. Isso prova que a API recebe telemetria de uma estacao real do dominio e grava no Oracle.

### POST /api/riscos/avaliar/8

Abra Riscos e execute POST /api/riscos/avaliar/8. O retorno esperado e uma lista de avaliacoes para ENCHENTE, DESLIZAMENTO, TEMPESTADE e QUALIDADE_AR, com score, nivel e motivo. Isso prova a regra de negocio de avaliacao de risco.

### GET /api/regioes/8/risco-atual

Abra Riscos e execute GET /api/regioes/8/risco-atual. O retorno esperado e o risco consolidado da regiao com as ultimas avaliacoes por tipo. Isso mostra o resultado final consumido por frontend/mobile.

### GET /api/alertas?idRegiao=8

Abra Alertas e execute GET /api/alertas com idRegiao=8. O retorno esperado e a lista de alertas ativos da regiao, se houver risco ALTO ou CRITICO. Isso prova que a avaliacao de risco gera alerta operacional.

### GET /api/dashboard/summary

Abra Dashboard e execute GET /api/dashboard/summary. O retorno esperado e um resumo com totais operacionais e maior nivel de risco. Isso prova o endpoint agregado para dashboard web/mobile.

### MQTT evidence

No Wokwi, mostre o ESP32 recebendo mensagem no topico app/estacoes/AMANAJE-SP-RP-001/alertas. Isso nao acontece pelo Swagger, mas prova que o backend Java tambem opera de forma assincrona via MQTT: consome telemetria, salva, calcula risco e publica comando preventivo.

## What NOT to show in the video

Nao gaste tempo mostrando:

* todos os endpoints CRUD;
* todos os endpoints GET;
* todas as classes model;
* todos os logs SQL;
* logs completos do deploy no Render;
* explicacao longa de cada campo;
* experimentos que falharam;
* APP-ST-001.

## Optional CRUD note

O projeto possui CRUD para clientes, usuarios, regioes e estacoes. O video nao precisa demonstrar POST/PUT/DELETE completo, porque o requisito principal e demonstrar proposta, arquitetura e funcionamento da API.

Se sobrar tempo e for necessario mostrar um CRUD rapido, use somente Estacoes IoT. E o recurso mais direto para demonstrar criacao, consulta, atualizacao e exclusao logica sem desviar do fluxo principal.

## Troubleshooting

* Render cold start: se a primeira chamada demorar, aguarde alguns minutos e tente /api/health novamente.
* CORS: se o frontend/Expo web falhar, confira CORS_ALLOWED_ORIGINS no Render.
* MQTT enabled on Render: confirme MQTT_ENABLED=true e os topicos configurados por variavel de ambiente.
* stationCode must exist: use AMANAJE-SP-RP-001, que e a estacao conhecida para o demo.
* Duplicate station code: se fizer CRUD opcional, troque o sufixo do codigo da estacao.
* Swagger POST validation errors: confira nomes dos campos, enums, limites de validacao e IDs existentes.
* Oracle constraints: erros de chave, obrigatoriedade ou unicidade indicam que o payload precisa respeitar o schema Oracle atual.
