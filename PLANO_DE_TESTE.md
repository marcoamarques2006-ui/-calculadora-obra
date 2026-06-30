# Plano de Teste — Calculadora de Materiais para Obra Residencial

**Disciplina:** Desenvolvimento de Sistemas
**Autor:** Marco Marques
**Data de execução:** 30/06/2026
**Versão da aplicação:** 0.0.1-SNAPSHOT (Spring Boot 3.4.5 + Jakarta Faces / JoinFaces 5.5.15)

---

## 1. Objetivo

Validar os serviços de cálculo de materiais (concreto da fundação e tijolos das paredes), a
geração e persistência de **orçamentos**, a consulta por **número** e por **nome do usuário**, o
**frontend em Jakarta Faces (JSF)** e o tratamento de erros de validação.

## 2. Ambiente de teste

| Item | Valor |
|------|-------|
| SO | Windows 11 |
| JDK | Eclipse Temurin 17.0.19 |
| Build | Maven Wrapper (`mvnw`) |
| Banco | H2 em memória (`create-drop`) |
| URL base | `http://localhost:8080` |
| Telas JSF | `/index.xhtml`, `/orcamento.xhtml`, `/consulta.xhtml` |
| API REST | `/api/orcamentos`, `/api/fundacao`, `/api/paredes`, `/api/planta-baixa` |
| Documentação | Swagger UI `/swagger-ui.html` |

## 3. Estratégia

- **Testes unitários automatizados** (JUnit 5 + Mockito) para as regras de cálculo e de orçamento.
- **Testes de integração manuais** via HTTP (REST) e navegador (telas JSF), com coleta de evidências.

---

## 4. Casos de teste automatizados (JUnit)

Executados com `mvnw test`. Resultado: **24 testes, 0 falhas**.

| ID | Classe | O que valida |
|----|--------|--------------|
| UT-01 | `ArestaTest` (6) | Área bruta, área de aberturas, área líquida e proteção contra área negativa |
| UT-02 | `CalculoConcreteServiceImplTest` (5) | Volume `espessura×alturaViga×comprimento`, soma de arestas, erros |
| UT-03 | `CalculoTijolosServiceImplTest` (6) | Tijolos/m², fator de perda 10%, desconto de aberturas, erros |
| UT-04 | `OrcamentoServiceImplTest` (6) | Custos, margem de lucro, persistência, busca por número/nome |
| UT-05 | `CalculadoraObraApplicationTests` (1) | Subida do contexto Spring + JSF |

### Evidência UT (saída do Maven)

```
[INFO] Tests run: 5, ... CalculoConcreteServiceImplTest
[INFO] Tests run: 6, ... CalculoTijolosServiceImplTest
[INFO] Tests run: 6, ... OrcamentoServiceImplTest
[INFO] Tests run: 1, ... CalculadoraObraApplicationTests
[INFO] Tests run: 6, ... ArestaTest
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 5. Casos de teste de integração (manuais)

### CT-01 — Renderização das telas JSF

**Passos:** acessar as três páginas `.xhtml`.
**Esperado:** HTTP 200 e HTML gerado pelo FacesServlet (com `<form>` e `ViewState`).

**Evidência:**
```
200  http://localhost:8080/index.xhtml      (len=3608)
200  http://localhost:8080/orcamento.xhtml  (len=7889)
200  http://localhost:8080/consulta.xhtml   (len=3934)

<form id="form" name="form" method="post" action="/orcamento.xhtml;jsessionid=...">
<input type="hidden" name="form" value="form" />
<label for="nome">Nome do usuário *</label><input id="form:nome" type="text" .../>
```
**Resultado:** ✅ PASSOU

---

### CT-02 — Gerar orçamento (cálculo + persistência)

**Entrada:** `POST /api/orcamentos` — cliente "Marco Marques", 4 paredes (uma com porta,
uma com janela), altura da viga 0,40 m, tijolo 0,057×0,19 m, concreto R$450/m³,
tijolo R$1,20, margem 20%.

**Esperado:** orçamento criado com número, totais calculados e itens por parede.

**Evidência (resposta):**
```json
{
  "numero": 1,
  "nomeUsuario": "Marco Marques",
  "volumeConcretoM3": 1.12,
  "quantidadeTijolos": 3905,
  "quantidadeTijolosComPerda": 4296,
  "areaTotalLiquida": 52.31,
  "custoConcreto": 504.0,
  "custoTijolos": 5155.2,
  "custoTotalMateriais": 5659.2,
  "margemLucroPercentual": 20.0,
  "valorFinalComMargem": 6791.04,
  "itens": [ P1..P4 com area, volume e tijolos ]
}
```
**Conferência manual:** concreto 0,336+0,224+0,336+0,224 = **1,12 m³** → ×450 = **R$504,00**;
4296 tijolos × R$1,20 = **R$5.155,20**; materiais **R$5.659,20** × 1,20 = **R$6.791,04**. ✔
**Resultado:** ✅ PASSOU

---

### CT-03 — Buscar orçamento por número

**Entrada:** `GET /api/orcamentos/1`
**Esperado:** retorna o orçamento 1 com `valorFinalComMargem = 6791.04`.
**Evidência:** `6791.04`
**Resultado:** ✅ PASSOU

---

### CT-04 — Buscar orçamento por nome do usuário (parcial, case-insensitive)

**Entrada:** `GET /api/orcamentos?nome=marco`
**Esperado:** localiza "Marco Marques".
**Evidência:**
```
numero nomeUsuario   valorFinalComMargem
------ -----------   -------------------
     1 Marco Marques             6791.04
```
**Resultado:** ✅ PASSOU

---

### CT-05 — Validação de entrada (campos obrigatórios)

**Entrada:** `POST /api/orcamentos` sem `nomeUsuario` e sem `paredes`.
**Esperado:** HTTP 400 com mensagens de validação.
**Evidência:**
```
HTTP 400
{"erros":{"nomeUsuario":"Nome do usuário é obrigatório",
          "paredes":"Informe ao menos uma parede da planta"}}
```
**Resultado:** ✅ PASSOU

---

### CT-06 — Orçamento inexistente

**Entrada:** `GET /api/orcamentos/999`
**Esperado:** HTTP 404.
**Evidência:** `HTTP 404`
**Resultado:** ✅ PASSOU

---

## 6. Resumo

| Categoria | Total | Passou | Falhou |
|-----------|-------|--------|--------|
| Unitários (JUnit) | 24 | 24 | 0 |
| Integração (CT-01..06) | 6 | 6 | 0 |
| **Total** | **30** | **30** | **0** |

**Conclusão:** Todos os casos passaram. Os serviços de cálculo, a geração/persistência de
orçamentos, a busca por número e por nome do usuário, o frontend Jakarta Faces e o tratamento
de erros funcionam conforme o esperado.

> Para reexecutar: `mvnw test` (unitários) e `mvnw spring-boot:run` + os comandos HTTP/telas acima (integração).
