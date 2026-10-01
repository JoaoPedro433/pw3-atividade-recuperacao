# 🏋️ Atividade Prática: Desenvolvimento Incremental da API de Exercícios Físicos no Spring Boot

## Roteiro de Implementação por Etapas (Fatias Verticais) e Controle de Versão

**Disciplina:** Programação Web III (PW3)  
**Curso:** Técnico em Desenvolvimento de Sistemas  
**Instituição:** Etec Horácio Augusto da Silveira  
**Tecnologias:** Java 25, Spring Boot 4, Spring Data JPA, MapStruct, Lombok, Git/GitHub, H2 Database

---

# 🎯 Objetivo da Atividade

Desenvolver uma API de gerenciamento de **Exercícios Físicos**, adotando o fluxo profissional de trabalho com Git/GitHub e o conceito de **fatias verticais (Vertical Slices)**.

Em vez de construir todas as camadas de uma só vez, você implementará uma funcionalidade completa de ponta a ponta por vez, do Repository até o Controller, testará no Postman/Insomnia e realizará um commit específico para cada etapa.

O fluxo de trabalho será estruturado da seguinte forma:

- 🚀 **ETAPA 0 (Pré-requisito):** Fork, Clone, Criação da Branch e Preparação Git
- 🟢 **ETAPA 1:** Listagem de todos os exercícios aprovados (`GET /exercicios-fisicos`) + Commit 1
- 🟡 **ETAPA 2:** Consulta de exercício aprovado por ID (`GET /exercicios-fisicos/{id}`) + Commit 2
- 🔵 **ETAPA 3:** Cadastro de novo exercício (`POST /exercicios-fisicos`) + Commit 3
- 🟣 **ETAPA 4:**  Aprovação de Exercício
- 
Para isso, você construirá as seguintes peças da arquitetura:

- **DTOs:** com `record` Java
- **Mapper:** com MapStruct
- **Repository:** Spring Data JPA
- **Service:** regras e orquestração
- **Controller:** exposição da API HTTP

---

# 📐 Fluxo Arquitetural da Aplicação

Antes de iniciar o código, visualize o caminho percorrido pela informação em cada requisição:

```text
 [ Cliente HTTP: Postman / Front-end ]
              │   ▲
 (HTTP POST/GET)  │   │ (JSON / HTTP Status 200, 201, 404)
              ▼   │
     ┌──────────────────────────┐
     │ ExercicioFisicoController│
     │      Camada Web / REST   │
     └──────────────────────────┘
              │   ▲
  (Passa DTO) │   │ (Retorna DTO)
              ▼   │
     ┌──────────────────────────┐
     │   ExercicioFisicoService │
     │    Camada de Negócio     │
     └──────────────────────────┘
         │ ▲             │ ▲
         │ │             │ │
         ▼ │             ▼ │
   ┌──────────────┐  ┌──────────────────────────┐
   │    Mapper    │  │ ExercicioFisicoRepository│
   └──────────────┘  └──────────────────────────┘
                              │ ▲
                              ▼ │ SQL
                     ┌───────────────────┐
                     │      Banco H2     │
                     └───────────────────┘
```

---

# 🚀 ETAPA 0 — Preparação do Ambiente e Git

Antes de iniciar qualquer linha de código em Java, execute os passos de versionamento obrigatórios.


## 0.1 Clonar o Repositório Localmente

No terminal da sua máquina, faça o clone do seu repositório:

```bash
git clone https://github.com/<seu-usuario>/pw3-atividade-recuperacao.git
cd <seu-repositorio>
```

## 0.2 Criar e Trocar para a Branch de Avaliação

Crie uma nova branch seguindo o padrão:

```bash
git checkout -b seu_nome
```


Exemplo:

```bash
git checkout -b lucas
```

Verifique a branch atual:

```bash
git branch
```

O resultado deverá indicar:

```text
* lucas
```

Altere o arquivo `joao.md` com o nome do integrante.

> [!IMPORTANT]
> ### Regra Obrigatória de Avaliação
>
> A cada etapa concluída, com código implementado e testado no Postman/Insomnia, você deverá realizar o commit com a mensagem indicada no roteiro.
>
> O histórico de commits da branch será avaliado.

---

# 🗂️ Recursos Pré-Existentes no Projeto

Para esta atividade, considere que a entidade JPA `ExercicioFisico` já foi criada previamente.

## 1. Entidade JPA: `ExercicioFisico.java`

A entidade possui a seguinte estrutura:

```java
import jakarta.persistence.*;

@Entity
@Table(name = "TBL_EXERCICIO_FISICO")
public class ExercicioFisico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EXERCICIO_FISICO")
    private Long id;

    @Column(name = "TX_NOME")
    private String nome;

    @Column(name = "TX_GRUPO_MUSCULAR")
    private String grupoMuscular;

    @Column(name = "TX_IMAGEM")
    private String imagem;

    @Column(name = "TX_DESCRICAO")
    private String descricao;

    @Column(name = "NR_SERIES")
    private Integer series;

    @Column(name = "NR_REPETICOES")
    private int repeticoes;

    @Column(name = "NR_CARGA_SUGERIDA")
    private double cargaSugerida;

    @Column(name = "TP_DIFICULDADE")
    private NivelDificuldadeEnum nivelDificuldade;

    @Column(name = "CK_APROVADO")
    private boolean aprovado;

}
```

## 2. Regra de Aprovação

Para esta atividade, um exercício será considerado aprovado quando:

```text
aprovado = true
```

Portanto:

- Exercícios aprovados podem aparecer nos endpoints de consulta.
- Exercícios não aprovados não devem aparecer na listagem.
- Exercícios não aprovados não devem ser retornados na consulta por ID.

> [!IMPORTANT]
> A existência do registro no banco não significa que ele esteja disponível na API.
>
> Para as consultas públicas desta atividade, somente exercícios com `aprovado = true` deverão ser considerados.

## 3. Enum `NivelDificuldadeEnum`

A entidade utiliza o enum:

```text
NivelDificuldadeEnum
```

Uma possível estrutura é:

```java
public enum NivelDificuldadeEnum {

    INICIANTE,
    INTERMEDIARIO,
    AVANCADO

}
```

> [!NOTE]
> Utilize os valores que já estiverem definidos no projeto-base.
>
> Caso o enum deva ser armazenado no banco como texto, utilize:
>
> ```java
> @Enumerated(EnumType.STRING)
> ```
>
> Caso nenhuma estratégia seja definida, o JPA utiliza o ordinal do enum por padrão.

---

# 📦 DTOs

Para esta atividade serão utilizados dois DTOs: Um de request e outro de response

O atributo `aprovado` não precisa ser retornado, pois as consultas desta atividade trabalham exclusivamente com exercícios aprovados.

---

# 🟢 ETAPA 1 — Listagem de Exercícios Aprovados

## Endpoint

```http
GET /exercicios-fisicos
```

Nesta etapa, você criará a estrutura necessária para listar todos os exercícios aprovados.

### Fluxo

```text
Postman
   │
   │ GET /exercicios-fisicos
   ▼
┌─────────────────────────────┐
│ ExercicioFisicoController   │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│ ExercicioFisicoService      │
└──────────────┬──────────────┘
               │
        ┌──────┴──────┐
        ▼             ▼
┌──────────────┐ ┌──────────────────────────┐
│    Mapper    │ │ ExercicioFisicoRepository│
└──────────────┘ └────────────┬─────────────┘
                              │
                              ▼
                     ┌─────────────────┐
                     │     Banco H2    │
                     └─────────────────┘
```

## 1.1 Repository

Pacote:

```text
br.com.etechoracio.<projeto>.repository
```

Crie a interface Repository


Crie um **Query Method** que busque somente os exercícios aprovados.


## 1.2 Mapper

Pacote:

```text
br.com.etechoracio.<projeto>.mapper
```

Crie: Mapper


## 1.3 Service

Pacote:

```text
br.com.etechoracio.<projeto>.service
```

Crie a classe de serviço: Service

Implemente um método para buscar todos os exercícios aprovados.

A lógica deverá:

1. Chamar o Repository.
2. Obter os exercícios aprovados.
3. Utilizar o Mapper.
4. Converter as entidades em DTOs.
5. Retornar a lista de DTOs.

## 1.4 Controller

Pacote:

```text
br.com.etechoracio.<projeto>.controller
```

Crie o Controller

---

## 🧪 Teste da Etapa 1

Inicie o projeto e realize:

```http
GET http://localhost:8080/exercicios-fisicos
```

### Status esperado

```text
200 OK
```

### Exemplo de resposta

```json
[
    {
        "id": 1,
        "nome": "Supino Reto",
        "grupoMuscular": "Peitoral",
        "imagem": "supino-reto.jpg",
        "descricao": "Exercício para desenvolvimento do peitoral.",
        "series": 4,
        "repeticoes": 12,
        "cargaSugerida": 20.0,
        "nivelDificuldade": "INICIANTE"
    }
]
```

Caso não existam exercícios aprovados:

```json
[]
```

## 📌 Commit da Etapa 1

```bash
git add .
git commit -m "feat: etapa 1 - listagem de exercicios aprovados (GET /exercicios-fisicos)"
git push -u origin alu1-alu2-av
```

---

# 🟡 ETAPA 2 — Consulta de Exercício Aprovado por ID

## Endpoint

```http
GET /exercicios-fisicos/{id}
```

Nesta etapa será implementada a consulta individual.

A regra continua sendo:

```text
id = informado
AND
aprovado = true
```

### Fluxo

```text
GET /exercicios-fisicos/1
             │
             ▼
┌─────────────────────────────┐
│ ExercicioFisicoController   │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│ ExercicioFisicoService      │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│ ExercicioFisicoRepository   │
└──────────────┬──────────────┘
               │
               ▼
           Banco H2
```

**Exercício aprovado encontrado → `200 OK`**

**Não encontrado/não aprovado → `404 Not Found`**

## 2.1 Repository

Na interface `ExercicioFisicoRepository`, crie um método derivado que busque por:

- `id`
- `aprovado = true`

O retorno deverá ser:

```java
Optional<ExercicioFisico>
```

## 2.2 Mapper

No `ExercicioFisicoMapper`, utilize o método para converter Exercício Físico em DTO


## 2.3 Service

Na classe `Service`, implemente a consulta por ID.

A lógica deverá:

1. Receber o ID.
2. Consultar o Repository.
3. Obter um `Optional<ExercicioFisico>`.
4. Converter a entidade encontrada para DTO.
5. Retornar o resultado.

Caso não exista exercício aprovado com o ID informado, o resultado deverá representar a ausência do recurso.

## 2.4 Controller

Na classe `Controller`, adicione um endpoint que deverá:

- Retornar `200 OK` quando encontrar o exercício.
- Retornar `404 Not Found` quando não encontrar.
- Considerar como não encontrado um exercício existente, porém não aprovado.

---

## 🧪 Testes da Etapa 2

### Cenário 1 — Exercício aprovado

```http
GET http://localhost:8080/exercicios-fisicos/1
```

**Esperado:**

```text
200 OK
```

### Cenário 2 — Exercício inexistente

```http
GET http://localhost:8080/exercicios-fisicos/999
```

**Esperado:**

```text
404 Not Found
```

### Cenário 3 — Exercício não aprovado

Caso exista um exercício com:

```text
aprovado = false
```

execute:

```http
GET /exercicios-fisicos/{id}
```

**Esperado:**

```text
404 Not Found
```

> [!IMPORTANT]
> O exercício existir no banco não significa que possa ser retornado pela API.
>
> Para esta atividade, somente exercícios aprovados estão disponíveis para consulta.

## 📌 Commit da Etapa 2

```bash
git add .
git commit -m "feat: etapa 2 - consulta de exercicio aprovado por id (GET /exercicios-fisicos/{id})"
git push origin alu1-alu2-av
```

---

# 🔵 ETAPA 3 — Cadastro de Novo Exercício

## Endpoint

```http
POST /exercicios-fisicos
```

Nesta etapa será implementado o cadastro de novos exercícios.

### Fluxo

```text
Postman
   │
   │ POST /exercicios-fisicos
   │ JSON
   ▼
┌─────────────────────────────┐
│ ExercicioFisicoController   │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│ ExercicioFisicoService      │
└──────────────┬──────────────┘
               │
        ┌──────┴──────┐
        ▼             ▼
┌──────────────┐ ┌──────────────────────────┐
│    Mapper    │ │ ExercicioFisicoRepository│
└──────────────┘ └────────────┬─────────────┘
                              │
                              ▼
                     ┌─────────────────┐
                     │     Banco H2    │
                     └─────────────────┘
```

## 3.1 Request DTO

Crie o request


## 3.2 Regra para Novo Exercício

Todo novo exercício deverá ser criado inicialmente como:

```text
aprovado = false
```

Isso significa que o exercício recém-cadastrado:

- Será salvo no banco.
- Receberá um ID.
- Não aparecerá em `GET /exercicios-fisicos`.
- Não poderá ser consultado por `GET /exercicios-fisicos/{id}`.

> [!NOTE]
> A aprovação poderá ser implementada em uma etapa futura.

## 3.3 Mapper

No `Mapper`, adicione o método para converter DTO em exercício físico


## 3.4 Service

Na classe `Service`, implemente o método de criação.

A sequência deverá ser:

1. Receber o `Request DTO`.
2. Converter o DTO para entidade.
3. Garantir que `aprovado = false`.
4. Salvar a entidade utilizando `repository.save(...)`.
5. Receber a entidade com o ID gerado.
6. Converter a entidade salva para `Response DTO`.
7. Retornar o DTO.

## 3.5 Controller

Na classe `Controller`, adicione o endpoint que deverá:

1. Receber o JSON.
2. Converter para `Request DTO`.
3. Invocar o Service.
4. Retornar o exercício criado.

Utilizar o status:

```text
201 Created
```

---

## 🧪 Teste da Etapa 3

No Postman/Insomnia:

### Método

```text
POST
```

### URL

```text
http://localhost:8080/exercicios-fisicos
```

### Header

```text
Content-Type: application/json
```

### Corpo

```json
{
    "nome": "Rosca Direta",
    "grupoMuscular": "Bíceps",
    "imagem": "rosca-direta.jpg",
    "descricao": "Exercício para desenvolvimento da musculatura do bíceps.",
    "series": 3,
    "repeticoes": 12,
    "cargaSugerida": 10.0,
    "nivelDificuldade": "INICIANTE"
}
```

### Status esperado

```text
201 Created
```

### Exemplo de resposta

```json
{
    "id": 3,
    "nome": "Rosca Direta",
    "grupoMuscular": "Bíceps",
    "imagem": "rosca-direta.jpg",
    "descricao": "Exercício para desenvolvimento da musculatura do bíceps.",
    "series": 3,
    "repeticoes": 12,
    "cargaSugerida": 10.0,
    "nivelDificuldade": "INICIANTE"
}
```

---

# 🔎 Verificação da Regra de Aprovação

Após realizar o cadastro:

```http
POST /exercicios-fisicos
```

execute:

```http
GET /exercicios-fisicos
```

O exercício recém-cadastrado **não deverá aparecer**, pois:

```text
aprovado = false
```

Também execute:

```http
GET /exercicios-fisicos/{id}
```

O resultado esperado será:

```text
404 Not Found
```

enquanto o exercício permanecer não aprovado.

## 📌 Commit da Etapa 3

```bash
git add .
git commit -m "feat: etapa 3 - cadastro de exercicio fisico (POST /exercicios-fisicos)"
git push origin alu1-alu2-av
```

---

# 🟣 ETAPA 4 — Aprovação de Exercício

## Endpoint

```http
PATCH /exercicios-fisicos/{id}/aprovar
```

Nesta etapa será implementada a funcionalidade responsável por **aprovar um exercício físico previamente cadastrado**.

A aprovação altera o atributo aprovado para true.

Depois de aprovado, o exercício poderá aparecer nos endpoints públicos de consulta:

```http
GET /exercicios-fisicos
```

e:

```http
GET /exercicios-fisicos/{id}
```

### Fluxo

```text
Postman
   │
   │ PATCH /exercicios-fisicos/{id}/aprovar
   ▼
┌─────────────────────────────┐
│ ExercicioFisicoController   │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│ ExercicioFisicoService      │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│ ExercicioFisicoRepository   │
└──────────────┬──────────────┘
               │
               ▼
        ┌───────────────┐
        │    Banco H2   │
        └───────────────┘
```

## 4.1 Regra de Aprovação

O exercício deverá ser aprovado somente quando o ID informado existir.

Ao executar:

```http
PATCH /exercicios-fisicos/{id}/aprovar
```

a aplicação deverá:

1. Receber o ID do exercício.
2. Buscar o exercício pelo ID.
3. Verificar se o exercício existe.
4. Alterar `aprovado` para `true`.
5. Salvar a alteração no banco.
6. Retornar o exercício aprovado.

Caso o exercício não exista, deverá ser retornado:

```text
404 Not Found
```

### Regra importante

Um exercício já aprovado poderá continuar aprovado.

> [!IMPORTANT]
> O atributo `aprovado` não deverá ser recebido pelo cliente no corpo da requisição.
>
> A aprovação deve ser controlada pela aplicação por meio do endpoint específico.

## 4.2 Repository

Na interface `Repository`, não é obrigatório criar um novo método de busca

```java
Optional<ExercicioFisico> findById(Long id);
```

Utilize esse método para localizar o exercício que será aprovado.

Não é necessário criar um DTO específico para aprovação.

## 4.4 Service

Na classe `Service`, implemente o método aprovar
A lógica deverá seguir:

```text
Receber ID
    ↓
Buscar exercício pelo ID
    ↓
Exercício existe?
   / \
 NÃO  SIM
 ↓     ↓
404   aprovado = true
       ↓
   repository.save()
       ↓
     Mapper
       ↓
  ResponseDTO
```

> [!NOTE]
> A exceção e o tratamento de erros podem ser adaptados à estrutura do projeto.
>
> Caso o projeto utilize uma exceção própria, como `RecursoNaoEncontradoException`, utilize a exceção definida no projeto-base.

## 4.5 Controller

Na classe `Controller`, adicione a chamada ao método aprovar usando a anotação

```java
@PatchMapping("/{id}/aprovar")
```

### Status HTTP

| Situação | Status |
|---|---:|
| Exercício encontrado e aprovado | `200 OK` |
| Exercício inexistente | `404 Not Found` |

---

# 🧪 Testes da Etapa 4

## Cenário 1 — Aprovar exercício não aprovado

Primeiro, cadastre um novo exercício:

```http
POST http://localhost:8080/exercicios-fisicos
```

O exercício será criado com:

```text
aprovado = false
```

Supondo que o banco tenha gerado:

```text
id = 3
```

execute:

```http
PATCH http://localhost:8080/exercicios-fisicos/3/aprovar
```

### Status esperado

```text
200 OK
```

### Exemplo de resposta

```json
{
    "id": 3,
    "nome": "Rosca Direta",
    "grupoMuscular": "Bíceps",
    "imagem": "rosca-direta.jpg",
    "descricao": "Exercício para desenvolvimento da musculatura do bíceps.",
    "series": 3,
    "repeticoes": 12,
    "cargaSugerida": 10.0,
    "nivelDificuldade": "INICIANTE"
}
```

O atributo `aprovado` não aparece na resposta porque ele não faz parte do `DTO`.

## Cenário 2 — Verificar a aprovação na listagem

Após executar:

```http
PATCH /exercicios-fisicos/3/aprovar
```

execute:

```http
GET http://localhost:8080/exercicios-fisicos
```

O exercício aprovado deverá aparecer na lista.

## Cenário 3 — Consultar o exercício aprovado por ID

Execute:

```http
GET http://localhost:8080/exercicios-fisicos/3
```

### Status esperado

```text
200 OK
```

O exercício deverá ser retornado porque agora:

```text
aprovado = true
```

## Cenário 4 — Aprovar exercício inexistente

Execute:

```http
PATCH http://localhost:8080/exercicios-fisicos/999/aprovar
```

### Status esperado

```text
404 Not Found
```

---

# 🔎 Verificação Completa da Regra de Aprovação

A sequência abaixo deverá demonstrar o funcionamento completo da regra.

### 1. Cadastrar

```http
POST /exercicios-fisicos
```

Resultado:

```text
aprovado = false
```

### 2. Consultar lista

```http
GET /exercicios-fisicos
```

Resultado:

```text
O exercício NÃO aparece.
```

### 3. Consultar por ID

```http
GET /exercicios-fisicos/{id}
```

Resultado:

```text
404 Not Found
```

### 4. Aprovar

```http
PATCH /exercicios-fisicos/{id}/aprovar
```

Resultado:

```text
200 OK
aprovado = true
```

### 5. Consultar lista novamente

```http
GET /exercicios-fisicos
```

Resultado:

```text
O exercício passa a aparecer.
```

### 6. Consultar por ID novamente

```http
GET /exercicios-fisicos/{id}
```

Resultado:

```text
200 OK
```

## 📌 Commit da Etapa 4

```bash
git add .
git commit -m "feat: etapa 4 - aprovacao de exercicio fisico (PATCH /exercicios-fisicos/{id}/aprovar)"
git push origin alu1-alu2-av
```


# 📦 Entrega da Atividade

Antes da entrega, verifique o estado do projeto:

```bash
git status
```

Verifique o histórico:

```bash
git log --oneline -n 4
```

Verifique a branch:

```bash
git branch
```

Certifique-se de que todos os commits foram enviados:

```bash
git push origin seuNome
```

Compartilhe com o professor o repositório ou abra um **Pull Request**, conforme orientação da disciplina.

---

# 📊 Rubrica de Avaliação

| Critério / Etapa | Descrição e Requisitos | Pontuação |
|---|---|---|
| **Etapa 0: Git & GitHub** | Fork realizado, projeto clonado, branch criada no padrão `alu1-alu2-av` e commits individuais identificando cada etapa. | **I** |
| **Etapa 1: Listagem** | Repository, Mapper, Service e Controller retornando somente exercícios aprovados com status `200 OK`. | **R** |
| **Etapa 2: Consulta por ID** | Repository, Mapper, Service e Controller tratando `Optional` e diferenciando exercício aprovado encontrado (`200 OK`) de exercício inexistente ou não aprovado (`404 Not Found`). | **B** |
| **Etapa 3: Cadastro** | Criação do Request DTO, métodos no Mapper e Service, Controller com `@RequestBody`, persistência e retorno `201 Created`, criando o exercício inicialmente como não aprovado. | **MB** |
| **Etapa 4: Aprovação do exercício físico** | Será analisado e testado nas etapas 1, 2 e 3. | ** ** |


---

# 🧩 Resultado Esperado

Ao concluir as três etapas, a API deverá possuir os seguintes endpoints:

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/exercicios-fisicos` | Lista exercícios aprovados |
| `GET` | `/exercicios-fisicos/{id}` | Consulta exercício aprovado por ID |
| `POST` | `/exercicios-fisicos` | Cadastra novo exercício |
| `PATCH` | `/exercicios-fisicos/{id}/aprovar` | Aprova um exercício |

O fluxo completo deverá seguir:

```text
                  ┌───────────────────────┐
                  │      Cliente HTTP     │
                  │   Postman / Frontend  │
                  └───────────┬───────────┘
                              │
                              ▼
                  ┌───────────────────────┐
                  │ ExercicioFisico       │
                  │      Controller       │
                  └───────────┬───────────┘
                              │
                              ▼
                  ┌───────────────────────┐
                  │ ExercicioFisico       │
                  │       Service         │
                  └───────┬───────┬───────┘
                          │       │
                          ▼       ▼
                 ┌────────────┐ ┌─────────────────┐
                 │   Mapper   │ │   Repository    │
                 └────────────┘ └────────┬────────┘
                                         │
                                         ▼
                                ┌─────────────────┐
                                │     Banco H2    │
                                └─────────────────┘
```

A implementação deverá respeitar a **separação de responsabilidades entre as camadas** e o **desenvolvimento incremental proposto em cada etapa**.

---
