# 📦 API de Pedidos — Desafio Técnico X-Brain

API REST para criação de pedidos com geração automática de entregas via mensageria assíncrona.

---

## Como executar

> Necessário apenas **Docker** instalado.

```bash
docker-compose up --build
```

| Serviço | URL |
|---|---|
| API | http://localhost:8080 |
| RabbitMQ Admin | http://localhost:15672 |
| Banco H2 | http://localhost:8080/h2-console |

> Credenciais RabbitMQ: `guest` / `guest`
> Credenciais H2: usuário `sa`, senha em branco, JDBC URL `jdbc:h2:mem:pedidosdb`

---

## Endpoints

### Criar pedido
```http
POST /pedidos
Content-Type: application/json
```
```json
{
  "codigoCliente": 1,
  "listaProdutos": [
    { "codigoProduto": 101, "nome": "Produto A", "preco": 49.90 },
    { "codigoProduto": 102, "nome": "Produto B", "preco": 29.90 }
  ],
  "endereco": {
    "rua": "Rua das Flores",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "cep": "01001-000"
  }
}
```
Retorna `201 Created` com o pedido salvo e o `valorTotal` calculado automaticamente.

### Listar pedidos
```http
GET /pedidos
```

### Buscar por ID
```http
GET /pedidos/{id}
```

---

## Explicação da solução

O fluxo principal funciona assim:

1. O cliente envia um `POST /pedidos` com os dados do pedido
2. O `PedidoService` calcula o `valorTotal` somando os preços dos produtos
3. O pedido é persistido no banco de dados (H2)
4. O pedido é publicado na fila `pedidos-queue` do RabbitMQ como JSON
5. O `PedidoConsumer` escuta a fila de forma assíncrona, recebe o pedido e cria automaticamente uma `Entrega` com o ID do pedido e o endereço, persistindo no banco

Essa separação entre criação do pedido e criação da entrega via mensageria garante que as duas responsabilidades sejam desacopladas — o endpoint responde imediatamente ao cliente enquanto o processamento da entrega acontece em background.

---

## Decisões técnicas

**H2 (banco em memória)**
Escolhido por atender ao requisito do desafio sem necessidade de configuração externa. Em produção seria substituído por PostgreSQL ou MySQL.

**RabbitMQ com fila durable**
A fila foi declarada com `durable = true` para que as mensagens sobrevivam a reinicializações do broker. O conversor `Jackson2JsonMessageConverter` foi configurado tanto no producer quanto no consumer para garantir serialização/desserialização consistente em JSON.

**`@ElementCollection` com `FetchType.EAGER`**
Os produtos do pedido foram modelados como `@Embeddable` dentro de uma `@ElementCollection`, evitando a criação de uma entidade separada para um relacionamento simples. O `EAGER` foi necessário para evitar `LazyInitializationException` ao serializar a resposta.

**`@Embedded` para endereço**
O endereço foi modelado como `@Embeddable` pois é um value object — não faz sentido existir isoladamente sem um pedido ou entrega.

**Docker Compose com healthcheck**
O serviço da aplicação só sobe após o RabbitMQ estar saudável (`condition: service_healthy`), evitando falhas de conexão na inicialização.

---

## O que foi implementado

- ✅ API REST com criação e consulta de pedidos
- ✅ Cálculo automático do valor total
- ✅ Persistência no banco de dados (H2)
- ✅ Publicação do pedido na fila RabbitMQ após a criação
- ✅ Consumer assíncrono que gera a entrega automaticamente
- ✅ Validações nos campos obrigatórios (`@NotNull`, `@NotBlank`)
- ✅ Containerização completa com Docker e Docker Compose
- ✅ Testes unitários cobrindo as principais regras de negócio

---

## O que melhoraria com mais tempo

- **DTOs (Request/Response):** separar os objetos de entrada e saída da API das entidades JPA, evitando expor detalhes do banco e ganhando mais controle sobre o contrato da API
- **Tratamento de erros global:** adicionar um `@RestControllerAdvice` para retornar respostas padronizadas em caso de erro (ex: `404` ao buscar pedido inexistente em vez de `500`)
- **Testes de integração:** cobrir o fluxo completo da API com `@SpringBootTest`, incluindo a publicação e consumo da fila
- **Banco de dados persistente:** substituir o H2 por PostgreSQL para um cenário mais próximo de produção
- **Dead Letter Queue:** configurar uma DLQ no RabbitMQ para reprocessar mensagens que falharam no consumer

---

## Testes

```bash
mvn test
```

Os testes unitários cobrem o `PedidoService` com Mockito, validando o cálculo do valor total, a persistência, a publicação na fila e o comportamento para IDs inexistentes.
