# Roteiro do Vídeo — Java Advanced Amanajé

## Objetivo do video

Este roteiro prepara um video objetivo, entre 5 e 7 minutos, para atender ao requisito:

> "Crie um video de ate 10 minutos apresentando a proposta da solucao, arquitetura do projeto e demonstracao de funcionamento da API."

O video deve mostrar somente as evidencias mais fortes:

* o que o Amanaje resolve;
* como a API Java entra na arquitetura;
* como o projeto esta estruturado;
* como os testes automatizados provam estabilidade basica;
* como o Swagger prova que a API esta funcional;
* como a persistencia Oracle esta funcionando;
* como funciona o fluxo de risco e alerta;
* como o MQTT prova a integracao IoT com Wokwi/HiveMQ.

Nao ha problema se o video terminar antes de 7 minutos. O foco e clareza, nao preencher os 10 minutos.

## Timing sugerido — versao objetiva

0:00 - 0:40 — Proposta da solucao  
0:40 - 1:30 — Arquitetura geral  
1:30 - 2:20 — Estrutura do projeto Java  
2:20 - 2:50 — Testes automatizados  
2:50 - 4:20 — Swagger: consultas principais  
4:20 - 5:40 — Swagger: leitura IoT, risco e alerta  
5:40 - 6:30 — MQTT/Wokwi/HiveMQ  
6:30 - 7:00 — Encerramento e requisitos atendidos  

## Roteiro de fala curto

Ola, meu nome e [seu nome] e neste video eu vou apresentar a API Java do Amanaje, desenvolvida para a Global Solution.

O Amanaje monitora regioes vulneraveis contra riscos climaticos e ambientais, como enchentes, deslizamentos, tempestades e problemas de qualidade do ar. A proposta e combinar dados de sensores IoT, observacoes climaticas externas e regras de avaliacao de risco para apoiar acoes preventivas.

Na arquitetura, a API Java Spring Boot e o backend central. Ela expoe os endpoints REST, concentra as regras de negocio e persiste os dados no Oracle FIAP. O Oracle armazena clientes, usuarios, regioes monitoradas, estacoes IoT, leituras, observacoes climaticas, avaliacoes de risco, alertas e indicadores regionais.

O servico .NET/C# fornece observacoes climaticas externas para a API. O ESP32 simulado no Wokwi envia telemetria por MQTT usando HiveMQ. A API Java consome essa telemetria, salva a leitura, calcula o risco da regiao e publica um comando de volta para o dispositivo. No dispositivo, esse comando controla LED verde, LED vermelho, buzzer e tela.

O frontend e o mobile consomem endpoints de regioes, riscos, alertas e dashboard. A API esta publicada no Render, e nesta demonstracao eu vou usar o Swagger publico para provar que ela esta funcionando.

Primeiro, no repositorio, eu mostro rapidamente o README, o Dockerfile e o application.yml. O Dockerfile evidencia o deploy, e o application.yml mostra a conexao com Oracle, o ddl-auto como validate, CORS e configuracoes MQTT. Depois mostro a estrutura src/main/java/br/com/fiap/amanaje, organizada por dominios como clientes, regioes, estacoes, leituras, riscos, alertas e dashboard.

Para mostrar a arquitetura em camadas, eu abro um controller, um service, um repository, uma entidade e um DTO record. Isso mostra a separacao entre entrada HTTP, regra de negocio, persistencia JPA, modelo Oracle e contratos de request/response.

Antes da demonstracao da API, eu rodo .\mvnw.cmd clean test no terminal. O objetivo e mostrar que os testes automatizados estao passando e que existe uma validacao basica de estabilidade.

Agora abro o Swagger publico em https://gs-java-advanced.onrender.com/swagger-ui/index.html. Primeiro chamo o health em /api/health para confirmar que a API esta no ar no Render. Depois mostro consultas que trazem dados do Oracle: GET /api/clientes, GET /api/regioes e GET /api/estacoes/regiao/8. Aqui eu destaco a regiao 8 e a estacao AMANAJE-SP-RP-001, que sera usada no fluxo IoT.

Em seguida, mostro GET /api/regioes/8/leituras para provar que existe historico de telemetria persistido. Depois envio uma leitura perigosa com POST /api/leituras para a estacao AMANAJE-SP-RP-001. Essa leitura tem nivel de agua alto, inclinacao alta, vibracao, pressao baixa e particulados elevados.

Depois executo POST /api/riscos/avaliar/8. A API usa a ultima leitura IoT valida e, quando existe, a ultima observacao climatica recente para avaliar ENCHENTE, DESLIZAMENTO, TEMPESTADE e QUALIDADE_AR. Cada avaliacao recebe score e nivel de risco. Quando o nivel e ALTO ou CRITICO, a API gera alerta.

Para fechar esse fluxo, chamo GET /api/regioes/8/risco-atual e GET /api/alertas?idRegiao=8. Assim mostro o risco consolidado da regiao e os alertas gerados. Tambem mostro GET /api/dashboard/summary para evidenciar o endpoint usado por frontend e mobile.

Por fim, se o Wokwi estiver disponivel, mostro a parte MQTT. O ESP32 publica telemetria em app/estacoes/AMANAJE-SP-RP-001/telemetria. A API Java no Render consome essa mensagem e publica o comando em app/estacoes/AMANAJE-SP-RP-001/alertas. No Wokwi, eu mostro o dispositivo recebendo esse comando e acionando LED vermelho e buzzer quando o risco e critico.

Com isso, o video cobre a proposta da solucao, arquitetura, estrutura Java, testes, Swagger, Oracle, fluxo de risco e alerta, deploy no Render e integracao IoT com MQTT.

## What to show on screen — essential only

1. README.md
2. Dockerfile
3. src/main/resources/application.yml
4. src/main/java/br/com/fiap/amanaje package structure
5. One controller, for example LeituraIotController or RiscoController
6. One service, for example RiscoService
7. One repository, for example LeituraIotRepository or AvaliacaoRiscoRepository
8. One model/entity, for example LeituraIot or AvaliacaoRisco
9. One request/response DTO record, for example LeituraIotCreateRequest
10. MQTT package: src/main/java/br/com/fiap/amanaje/leituras/mqtt
11. Terminal running: .\mvnw.cmd clean test
12. Swagger public URL: https://gs-java-advanced.onrender.com/swagger-ui/index.html
13. Health endpoint: https://gs-java-advanced.onrender.com/api/health
14. GET /api/clientes
15. GET /api/regioes
16. GET /api/estacoes/regiao/8
17. POST /api/leituras
18. POST /api/riscos/avaliar/8
19. GET /api/alertas?idRegiao=8
20. GET /api/dashboard/summary
21. Wokwi terminal receiving the Java command on app/estacoes/AMANAJE-SP-RP-001/alertas, if available

## Checklist de requisitos atendidos

| Requisito | Evidencia no projeto/video |
| --- | --- |
| Spring Boot REST API | Controllers REST e Swagger publico |
| Layered architecture | Pacotes controller, service, repository, model e dto |
| DTOs / Java Records | Requests e responses implementados como records |
| Spring Validation | Anotacoes como @NotNull, @NotBlank, @Positive e @DecimalMin |
| Global exception handling | GlobalExceptionHandler e ApiErrorResponse |
| Spring Data JPA / JpaRepository | Repositories dos dominios principais |
| Oracle persistence | application.yml com Oracle e ddl-auto=validate |
| Multiple tables | Entidades JPA para clientes, usuarios, regioes, estacoes, leituras, observacoes, riscos, alertas e indicadores |
| CRUD endpoints | CRUDs de clientes, usuarios, regioes e estacoes |
| Swagger/OpenAPI | Swagger UI e /v3/api-docs publicados no Render |
| HATEOAS in selected endpoints | Links em endpoints selecionados, como estacoes |
| Docker / Render deploy | Dockerfile e URL publica do Render |
| Tests passing | Execucao de .\mvnw.cmd clean test |
| MQTT integration with Wokwi/HiveMQ | Pacote leituras/mqtt, topicos de telemetria, status e alertas |
| Risk and alert business flow | POST /api/riscos/avaliar/8 e GET /api/alertas?idRegiao=8 |
