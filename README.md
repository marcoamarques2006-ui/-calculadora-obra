# Calculadora de Obra

API REST para cálculo de materiais em obras residenciais. Modela a planta baixa como um **grafo G=(V,A)** onde vértices são pilares (encontros de paredes) e arestas são as paredes. Calcula volume de concreto para vigas baldrame e quantidade de tijolos por parede, com persistência completa via JPA/H2.

---

## Tecnologias

- Java 17
- Spring Boot 3.4.5
- **Jakarta Faces (JSF / Mojarra) via JoinFaces 5.5.15 — frontend**
- Spring Data JPA
- Spring Validation
- Banco de dados H2 (em memória)
- Springdoc OpenAPI / Swagger UI
- Maven

---

## Arquitetura

O projeto segue os princípios de **Clean Architecture** com separação em camadas:

```
src/main/java/marco/calculadora_obra/
├── api/
│   ├── controller/     # Controllers REST (PlantaBaixa, Fundacao, Parede)
│   └── dto/            # Objetos de request e response
├── application/
│   └── service/        # Implementações dos serviços
├── domain/
│   ├── model/          # Modelos de domínio (PlantaBaixa, Vertice, Aresta, Comodo)
│   └── service/        # Interfaces dos serviços
└── infrastructure/
    └── persistence/
        ├── entity/     # Entidades JPA (PlantaBaixaEntity, VerticeEntity, ArestaEntity, ComodoEntity)
        └── repository/ # Repositórios Spring Data JPA
```

---

## Modelo de domínio — Grafo G=(V,A)

```
PlantaBaixa
├── vertices: List<Vertice>   → pilares (encontros de paredes)
├── arestas:  List<Aresta>    → paredes (conectam dois vértices via origemId/destinoId)
└── comodos:  List<Comodo>    → cômodos com nome, largura, comprimento e altura
```

Cada `Aresta` conecta dois `Vertice` (origem → destino) e pode conter janela e/ou porta com suas dimensões.

### Tabelas no banco H2

| Tabela           | Descrição                                  |
|------------------|--------------------------------------------|
| `plantas_baixas` | Planta baixa (o grafo)                     |
| `vertices`       | Vértices (pilares)                         |
| `arestas`        | Arestas (paredes)                          |
| `comodos`        | Cômodos                                    |
| `comodo_arestas` | Relação N:N entre cômodos e paredes        |

---

## Como executar

### Pré-requisitos

- Java 17+

### Rodando a aplicação

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

---

## Frontend (Jakarta Faces / JSF)

Interface web para o usuário solicitar e consultar orçamentos, implementada em **Jakarta Faces**
(Mojarra) integrada ao Spring Boot via **JoinFaces**. As páginas `.xhtml` ficam em
`src/main/resources/META-INF/resources` e os backing beans em `marco.calculadora_obra.web`.

| Tela | URL | Função |
|------|-----|--------|
| Início | http://localhost:8080/ | Boas-vindas e navegação |
| Novo orçamento | http://localhost:8080/orcamento.xhtml | Formulário dinâmico: dados do cliente, paredes (com janela/porta), parâmetros e preços → gera o orçamento |
| Consulta | http://localhost:8080/consulta.xhtml | Busca orçamentos por **número** ou por **nome do usuário** |

## Orçamentos (persistência e busca)

O orçamento é calculado a partir da planta (paredes), combina o volume de concreto e a quantidade
de tijolos, aplica preços e margem de lucro, e é **persistido** (tabelas `orcamentos` e
`orcamento_itens`). Pode ser recuperado pelo **número** ou pelo **nome do usuário**.

`POST /api/orcamentos` · `GET /api/orcamentos/{numero}` · `GET /api/orcamentos?nome={nome}`

```json
{
  "nomeUsuario": "Marco Marques",
  "descricao": "Casa térrea 3 quartos",
  "alturaViga": 0.40, "alturaTijolo": 0.057, "comprimentoTijolo": 0.19,
  "precoConcretoM3": 450.0, "precoTijolo": 1.20, "margemLucroPercentual": 20.0,
  "paredes": [
    { "id": "P1", "comprimento": 6.0, "espessura": 0.14, "alturaParede": 2.8,
      "temPorta": true, "porta": { "altura": 2.1, "comprimento": 0.9 } }
  ]
}
```

**Fórmula do valor final:** `valor = (volumeConcreto×precoConcretoM3 + tijolosComPerda×precoTijolo) × (1 + margem/100)`

## Plano de teste

Veja [PLANO_DE_TESTE.md](PLANO_DE_TESTE.md) — 24 testes unitários (JUnit) + 6 casos de integração, todos com evidências.

## Documentação e ferramentas

| Ferramenta   | URL                                      |
|--------------|------------------------------------------|
| Frontend JSF | http://localhost:8080/                   |
| Swagger UI   | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs        |
| H2 Console   | http://localhost:8080/h2-console         |

**Credenciais H2:**
- JDBC URL: `jdbc:h2:mem:calculadoradb`
- Usuário: `sa`
- Senha: *(vazio)*

---

## Fluxo de uso recomendado

### 1. Salvar a planta baixa

`POST /api/planta-baixa`

```json
{
  "descricao": "Casa térrea 3 quartos",
  "vertices": [
    { "id": "V1", "x": 0.0, "y": 0.0 },
    { "id": "V2", "x": 6.0, "y": 0.0 },
    { "id": "V3", "x": 6.0, "y": 4.0 },
    { "id": "V4", "x": 0.0, "y": 4.0 }
  ],
  "arestas": [
    {
      "id": "P1", "origemId": "V1", "destinoId": "V2",
      "comprimento": 6.0, "espessura": 0.14, "alturaParede": 2.8,
      "temJanela": false, "temPorta": true,
      "porta": { "altura": 2.1, "comprimento": 0.9 }
    },
    {
      "id": "P2", "origemId": "V2", "destinoId": "V3",
      "comprimento": 4.0, "espessura": 0.14, "alturaParede": 2.8,
      "temJanela": true, "temPorta": false,
      "janela": { "altura": 1.2, "comprimento": 1.5 }
    },
    {
      "id": "P3", "origemId": "V3", "destinoId": "V4",
      "comprimento": 6.0, "espessura": 0.14, "alturaParede": 2.8,
      "temJanela": false, "temPorta": false
    },
    {
      "id": "P4", "origemId": "V4", "destinoId": "V1",
      "comprimento": 4.0, "espessura": 0.14, "alturaParede": 2.8,
      "temJanela": false, "temPorta": false
    }
  ],
  "comodos": [
    {
      "nome": "Sala", "largura": 6.0, "comprimento": 4.0, "altura": 2.8,
      "arestaIds": ["P1", "P2", "P3", "P4"]
    }
  ]
}
```

**Response:** retorna o grafo salvo com o `id` gerado (ex: `1`).

---

### 2. Calcular volume de concreto (fundação)

`POST /api/fundacao/volume-concreto`

Usando planta salva:
```json
{
  "plantaBaixaId": 1,
  "alturaViga": 0.40
}
```

Ou enviando arestas diretamente:
```json
{
  "arestas": [
    { "id": "P1", "comprimento": 6.0, "espessura": 0.14, "alturaParede": 2.8, "temJanela": false, "temPorta": false }
  ],
  "alturaViga": 0.40
}
```

**Fórmula:** `Volume = espessura × alturaViga × comprimento` por aresta.

**Response:**
```json
{
  "volumeTotalM3": 0.336,
  "totalArestas": 1,
  "detalhes": [
    { "arestaId": "P1", "comprimento": 6.0, "largura": 0.14, "altura": 0.4, "volume": 0.336 }
  ]
}
```

---

### 3. Calcular quantidade de tijolos

`POST /api/paredes/quantidade-tijolos`

Usando planta salva:
```json
{
  "plantaBaixaId": 1,
  "alturaTijolo": 0.057,
  "comprimentoTijolo": 0.19
}
```

**Response:**
```json
{
  "quantidadeTotalTijolos": 1450,
  "quantidadeComPerda": 1595,
  "areaTotalLiquida": 44.22,
  "detalhes": [...]
}
```

| Campo                    | Descrição                                        |
|--------------------------|--------------------------------------------------|
| `quantidadeTotalTijolos` | Tijolos sem fator de perda                       |
| `quantidadeComPerda`     | Tijolos com 10% de acréscimo para quebras        |
| `areaTotalLiquida`       | Área total descontando janelas e portas (m²)     |

---

### 4. Recuperar planta salva

`GET /api/planta-baixa/{id}`

Retorna o grafo completo com vértices, arestas e cômodos.

---

## Autor

Marco Marques