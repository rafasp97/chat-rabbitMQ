# 📡 Chat RabbitMQ (Spring Boot)

Projeto de chat simples em tempo real utilizando **Spring Boot + RabbitMQ**, onde usuários enviam mensagens entre si através de filas nomeadas por usuário.

---

## 🚀 Tecnologias usadas

- Java 26
- Spring Boot
- Spring AMQP (RabbitMQ)
- RabbitMQ Server
- Maven

---

## 📦 Pré-requisitos

Antes de rodar o projeto, você precisa ter instalado:

- Java 26
- Maven 3.9.15
- RabbitMQ Server

---

## 🐇 Subindo o RabbitMQ (recomendado via Docker)

```bash
docker run -d --hostname rabbit-host --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

## ▶️ Como rodar o projeto

### Clonar o repositório

```bash
git clone https://github.com/rafasp97/chat-rabbitMQ.git
```

---


### Rodar o servidor do RabbitMQ

```bash
docker start rabbitmq
```

---

### Inicar a aplicação

```bash
mvn spring-boot:run
```