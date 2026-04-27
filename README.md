# Calculadora de Obra

API REST para cálculo de materiais em obras residenciais. Calcula a quantidade de tijolos para paredes e o volume de concreto para vigas baldrame (fundação), considerando aberturas (janelas e portas) e fatores de perda.

---

## Tecnologias

- Java 17
- Spring Boot 4.0.6
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
│   ├── controller/     # Controllers REST
│   └── dto/            # Objetos de request e response
├── application/
│   └── service/        # Implementações dos serviços
├── domain/
│   ├── model/          # Modelos de domínio
│   └── service/        # Interfaces dos serviços
└── infrastructure/
    └── persistence/    # Entidades JPA e repositórios
```

---

## Como executar

### Pré-requisitos

- Java 17+
- Maven 3.8+

### Rodando a aplicação

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

---

## Documentação e ferramentas

| Ferramenta   | URL                                      |
|--------------|------------------------------------------|
| Swagger UI   | http://localhost:8080/swagger-ui.html    |
| OpenAPI JSON | http://localhost:8080/v3/api-docs        |
| H2 Console   | http://localhost:8080/h2-console         |

**Credenciais H2:**
- JDBC URL: `jdbc:h2:mem:calculadoradb`
- Usuário: `sa`
- Senha: *(vazio)*

---

## Endpoints

### POST `/api/paredes/quantidade-tijolos`

Calcula a quantidade de tijolos necessários para um conjunto de paredes, descontando aberturas (janelas e portas) e aplicando 10% de fator de perda.

**Request:**

```json
{
  "arestas": [
    {
      "id": "parede-1",
      "comprimento": 5.0,
      "espessura": 0.14,
      "alturaParede": 2.8,
      "temJanela": true,
      "janela": {
        "largura": 1.2,
        "altura": 1.2
      },
      "temPorta": false
    }
  ],
  "alturaTijolo": 0.057,
  "larguraTijolo": 0.14,
  "comprimentoTijolo": 0.19
}
```

**Response:**

```json
{
  "quantidadeTotalTijolos": 412,
  "quantidadeComPerda": 453,
  "areaTotalLiquida": 12.56,
  "detalhes": [
    {
      "idAresta": "parede-1",
      "areaLiquida": 12.56,
      "quantidadeTijolos": 412
    }
  ]
}
```

| Campo                | Descrição                                           |
|----------------------|-----------------------------------------------------|
| `quantidadeTotalTijolos` | Tijolos sem fator de perda                      |
| `quantidadeComPerda`     | Tijolos com 10% de acréscimo para perdas        |
| `areaTotalLiquida`       | Área total das paredes descontando aberturas    |

---

### POST `/api/fundacao/volume-concreto`

Calcula o volume de concreto necessário para as vigas baldrame da fundação.

**Fórmula:** `Volume = espessura × alturaViga × comprimento` por aresta.

**Request:**

```json
{
  "arestas": [
    {
      "id": "viga-1",
      "comprimento": 6.0,
      "espessura": 0.20,
      "alturaParede": 0.0
    },
    {
      "id": "viga-2",
      "comprimento": 4.0,
      "espessura": 0.20,
      "alturaParede": 0.0
    }
  ],
  "alturaViga": 0.40
}
```

**Response:**

```json
{
  "volumeTotalM3": 0.8,
  "totalArestas": 2,
  "detalhes": [
    {
      "idAresta": "viga-1",
      "volumeM3": 0.48
    },
    {
      "idAresta": "viga-2",
      "volumeM3": 0.32
    }
  ]
}
```

| Campo           | Descrição                               |
|-----------------|-----------------------------------------|
| `volumeTotalM3` | Volume total de concreto em m³          |
| `totalArestas`  | Número de vigas calculadas              |
| `detalhes`      | Volume individual por viga              |

---

## Modelo de dados — Aresta

| Campo         | Tipo    | Obrigatório | Descrição                          |
|---------------|---------|-------------|------------------------------------|
| `id`          | string  | Sim         | Identificador da parede/viga       |
| `comprimento` | double  | Sim         | Comprimento em metros              |
| `espessura`   | double  | Sim         | Espessura em metros                |
| `alturaParede`| double  | Sim         | Altura da parede em metros         |
| `temJanela`   | boolean | Não         | Indica se há janela na parede      |
| `janela`      | object  | Não         | Dimensões da janela (largura/altura)|
| `temPorta`    | boolean | Não         | Indica se há porta na parede       |
| `porta`       | object  | Não         | Dimensões da porta (largura/altura) |

---

## Autor

Marco Marques