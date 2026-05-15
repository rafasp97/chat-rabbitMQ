# ChatRabbitMQ: Primeira Etapa

O projeto ChatRabbitMQ visa a criação de um chat de mensagens a partir de linha de comando, utilizando como tecnologia o RabbitMQ, Java e a Instância EC2 que foi criada na sala de aula da disciplina na AWS Academy.

 Nesta primeira etapa, serão utilizadas filas individuais para o envio e recebimento de mensagens aos usuários.
 
 ## 1 - Iniciando o chat
Ao ser iniciado, o chat solicita o nome do usuário para prosseguir:

> User:
> User: katharine

A fila do usuário é criada no RabbitMQ, e a linha de comando exibe uma marcação para demonstrar que o chat foi iniciado: 
> <<

  
  ## 2 - Enviando mensagens a outros usuários 
 Para enviar uma mensagem a outro usuário, o remetente deve identificá-lo utilizando  ***"@"*** como prefixo ao nome do destinatário.

> @Maria <<


Ao identificar o destinatário, o sistema irá alterar o prompt da linha de comando automaticamente, para exibir o nome do usuário escolhido pelo remetente. Desta forma, a exibição do prompt
> @Maria<<

significa que as mensagens enviadas pelo usuário serão destinadas à Maria.

> @Maria << Oi Maria!! Tudo bem com você?


## 3 - Alternando o destinatário
Para alterar o destinatário, basta informar o nome no prompt seguindo o padrão com o prefixo ***"@"***.  
> @Maria << @Paulo

A conversa com o novo destinatário será aberta.

> @Paulo <<

## 4 - Visualizando as mensagens recebidas
As mensagens recebidas poderão ser exibidas a qualquer momento, independentemente do usuário com quem a conversa atual esteja aberta. Todas elas seguem este padrão:
> (09/05/2026 às 12:00) @usuario diz: 

> (09/05/2026 às 12:00) @Maria diz: Oii katharine!! Está tudo bem comigo sim, você quer vir aqui em casa hoje pata almoçarmos juntas?

Após a exibição de qualquer mensagem recebida pelo usuário, o prompt é alterado para o estado anterior

> (09/05/2026 às 12:00) @Maria diz: Oii katharine!! Está tudo bem comigo sim, você quer vir aqui em casa hoje pata almoçarmos juntas?

> @Paulo << 
