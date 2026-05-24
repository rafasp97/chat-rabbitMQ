# ChatRabbitMQ: Terceira Etapa

Nesta etapa será implementada a viabilidade de envios de arquivos através do chat.

A transmissão dos arquivos também será implementada através do Protocolo de Buffers, e os tipos MIME correspondentes ao formatos do arquivos enviados serão incluído no envio das mensagens.
  

## 1 - Enviando arquivos

  

Para enviar um arquivo em grupos ou em conversas diretas, o usuário deve digitar o comando
> !upload
> @Maria << !upload Trabalho.pdf

>  #Trabalho de SD << !upload Trabalho2.pdf

A execução do envio dos arquivos são sinalizadas com a mensagem
> Enviando "nome-do-arquivo.extensão" para <destinatário>

> Arquivo "nome-do-arquivo.extensão" foi enviado para @Rafael !

Após exibição da mensagem de envio,  o prompt do volta novamente para o grupo ou usuário. O processo de  envio de arquivos é realizado paralelamente ao envio de mensagens de texto, isto significa que, é possível enviar arquivos e mensagens de forma simultânea.

## 2-  Visualizando arquivos recebidos
Os arquivos recebidos são baixados automaticamente em uma pasta default do usuário, eles também são recebidos de forma paralela ao recebimento de mensagens de texto.
> (23/05/2026 às 22:15) Arquivo "Trabalho.pdf" recebido de @Maria!