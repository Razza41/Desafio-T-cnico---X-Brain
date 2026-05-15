# 📦 API de Pedidos — X-Brain

API REST para criação de pedidos com geração automática de entregas via mensageria assíncrona.

---

## Como funciona

1. Cliente envia um pedido via `POST /pedidos`
2. O sistema calcula o valor total e salva o pedido no banco
3. O pedido é publicado numa fila do RabbitMQ
4. Um consumer lê a fila e cria automaticamente a entrega

---

## Rodando o projeto

> Necessário apenas **Docker** instalado.

```bash
docker-compose up --build
```

| Serviço | URL |
|---|---|
| API | http://localhost:8080 |
| RabbitMQ | http://localhost:15672 |
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

## Testes

```bash
mvn test
```

---

## Tecnologias

- Java 21 + Spring Boot 3.4.5
- RabbitMQ (mensageria)
- H2 (banco em memória)
- Docker + Docker Compose
