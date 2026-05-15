# ChatRabbitMQ: Segunda Etapa

  Nesta segunda etapa, será implementado o chat em grupos, permitindo que usuários criem grupos e adicionem membros a eles.

A transmissão das mensagens será implementada através do Protocolo de Buffers, e novos campos também serão incluídos no envio das mensagens, como data e hora de envio e o nome do remetente.

  

## 1- Criando grupos

Para criar um novo grupo, o usuário deve digitar o comando 

> !addGroup

Toda entrada iniciada com "**!**" é interpretada como um comando. 
Desta forma, nenhuma mensagem será enviada ao destinatário exibido pelo prompt no momento da execução.  

> @Maria << !addGroup Trabalho de SD
## 2- Adicionando usuários em grupos
Para adicionar usuários em grupos, deve ser utilizado o comando "!addUser", seguido do nome do usuário a ser adicionado e do grupo em que ele será inserido.

> @Maria << !addUser nomeDoUsuario nomeDoGrupo
> @Maria <<  !addUser Rafael Trabalho de SD

## 3- Enviando mensagens em grupos
Para acessar um grupo e enviar mensagens a ele, utilize o prefixo "#" seguido do nome do grupo.

> @Maria << #Trabalho de SD

Ao identificar o grupo, o sistema irá alterar o prompt da linha de comando automaticamente, para exibir o nome do grupo escolhido pelo remetente. Desta forma, a exibição do prompt
> #Trabalho de SD <<

significa que as mensagens enviadas pelo usuário serão destinadas ao grupo Trabalho de SD.
> #Trabalho de SD << Oii gente, o dia de entrega do trabalho é sábado.

O envio de mensagens para um grupo é realizado através de uma única mensagem enviada ao exchange do grupo, que será responsável por enviá-las para as filas de cada um dos integrantes.

## 4 - Visualizando as mensagens recebidas
As mensagens recebidas de grupos poderão ser exibidas a qualquer momento, independentemente do usuário com quem a conversa atual esteja aberta. Todas elas seguem este padrão:
> (09/05/2026 às 12:00) usuario#NomeDoGrupo diz:
> 
> (09/05/2026 às 21:50) Rafael#Trabalho de SD diz: Simm, precisamos enviar o link do repositório no classroom também
## 5- Removendo usuários de grupos
Para remover usuários em grupos, deve ser utilizado o comando *****"!removeUser"***,** seguido do nome do usuário a ser adicionado e do grupo em que ele será inserido.
> @Maria << !removeUser nomeDoUsuario nomeDoGrupo
>
> @Maria <<  !removeUser Rafael Trabalho de SD

## 6- Excluindo um grupo
Para excluir um grupo, deve ser utilizado o comando **"!removeGroup"**, seguido do nome do grupo a ser excluído.

> @Maria <<  !removeGroup  Trabalho de SD
