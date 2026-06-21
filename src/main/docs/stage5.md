# ChatRabbitMQ: Quinta Etapa

Nesta etapa foi implementada a funcionalidade de consulta de grupos e usuários no chat, utilizando a API HTTP de gerenciamento do RabbitMQ como fonte de dados principal.

## 1 - Integração com a API de gerenciamento do RabbitMQ

Para viabilizar essas operações, foi utilizada a Management HTTP API do RabbitMQ, permitindo consultar em tempo real:

    -Exchanges (grupos do chat)
    -Queues (usuários do sistema)
    -Bindings (relações entre usuários e grupos)

## 2 - Listando grupos do usuário

Foi adicionada a operação 

>!listGroups

Exemplo: 

>@Vinicius<< !listGroups
>ufs, amigos, familia

Essa funcionalidade consulta a API de gerenciamento do RabbitMQ e recupera todos os grupos (exchanges) aos quais o usuário corrente está associado. A identificação dos grupos é feita a partir dos bindings da fila do usuário, onde cada exchange vinculada à sua queue representa um grupo do qual ele participa.

## 3 - Listando usuários de um grupo

Foi adicionada a operação

>!listUsers <grupo>

Exemplo:

>@Vinicius<< !listUsers ufs
>Vinicius, Suzy, Rafael

Essa funcionalidade consulta a API de gerenciamento do RabbitMQ e recupera todos os usuários pertencentes a um determinado grupo. A lista é construída a partir dos bindings da exchange do grupo, filtrando apenas as destinations do tipo queue, que representam os usuários conectados ao grupo.