package sd_ufs.chat_rabbitmQ.services;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.scheduling.annotation.Async;
import com.rabbitmq.client.Channel;
import org.springframework.stereotype.Service;
import sd_ufs.chat_rabbitmQ.model.BodyMessage;
import sd_ufs.chat_rabbitmQ.utils.Utils;
import chat.Message;

@Service
public class RabbitService {

    private final RabbitAdmin rabbitAdmin;
    private final RabbitTemplate rabbitTemplate;
    private final SimpleMessageListenerContainer container;

    public RabbitService(
            RabbitAdmin rabbitAdmin,
            RabbitTemplate rabbitTemplate,
            SimpleMessageListenerContainer container
    ) {
        this.rabbitAdmin = rabbitAdmin;
        this.rabbitTemplate = rabbitTemplate;
        this.container = container;
    }

    public boolean queueExists(String queueName) {
        return this.rabbitAdmin.getQueueInfo(queueName) != null;
    }

    public void createQueue(String queueName) {
        if (!queueExists(queueName)) {
            Queue queue = new Queue(queueName, true);
            this.rabbitAdmin.declareQueue(queue);
        }
    }

    public void sendMessage(String sendBy, String sendTo, BodyMessage msg, char prefix) {

        //TO REMEMBER: this.rabbitTemplate.convertAndSend(exchange, queue, message);

        if(prefix == '@'){
            Message message = Utils.prepareToSend(sendBy, "", msg);
            this.sendMessageToQueue(sendTo, message);
            return;
        } 

        if(prefix == '#') {
            Message message = Utils.prepareToSend(sendBy, sendTo, msg);
            this.sendMessageToExchange(sendTo, message);
            return;
        }

        System.out.println("Unknown Prefix " + prefix);
    }

    @Async
    public void sendMessageToQueue(String sendTo, Message message){
        this.rabbitTemplate.convertAndSend("", sendTo, message.toByteArray());
        this.handleMessageAfterFileSubmission(sendTo, message);
    }

    @Async
    public void sendMessageToExchange(String sendTo, Message message){
        this.rabbitTemplate.convertAndSend(sendTo, "", message.toByteArray());
        this.handleMessageAfterFileSubmission(sendTo, message);
    }

    @Async
    public void handleMessageAfterFileSubmission(String sendTo, Message message){
        if(message.getContent().getType() == "text/plain") return;
        System.out.println("The file '" + message.getContent().getName() + "' was sent to " + sendTo + " !");
    }

    public void consumeMessages(String queueName, Runnable determinePrefix) {
        this.container.stop();
        this.container.setQueueNames(queueName);
        //setMessageListener: define uma função que determina o que fazer quando uma mensagem chegar.
        this.container.setMessageListener(msg -> {
            try {
                Message message = Message.parseFrom(msg.getBody());
                if (!message.getIssuer().equals(queueName)) {
                    System.out.println("\n" + Utils.formatMessage(message, queueName));
                    determinePrefix.run();
                }
            } catch (Exception e) {
                System.out.println("Invalid message in RabbitMq service");
            }
        });
        this.container.start();
    }

    public boolean exchangeExists(String exchangeName) {
        try {
            return rabbitTemplate.execute(channel -> {
                ((Channel) channel).exchangeDeclarePassive(exchangeName);
                return true;
            });
        } catch (Exception e) {
            return false;
        }
    }

    public void createExchange(String exchangeName, String queueName) {
        FanoutExchange exchange = new FanoutExchange(exchangeName);
        this.rabbitAdmin.declareExchange(exchange);
        this.bindQueueToExchange(exchangeName, queueName);
    }

    public void deleteExchange(String exchangeName) {
        this.rabbitAdmin.deleteExchange(exchangeName);
    }

    public void bindQueueToExchange(String exchangeName, String queueName) {
        Binding binding = BindingBuilder
                .bind(new Queue(queueName))
                .to(new FanoutExchange(exchangeName));
        //O 'new Queue' não cria uma nova fila, apenas faz referência a uma fila existente (objeto Queue);
        //O rabbit depende do this.rabbitAdmin para fazer alterações no servidor rabbit.
        this.rabbitAdmin.declareBinding(binding);
    }

    public void unbindQueueFromExchange(String exchangeName, String queueName) {
        Binding binding = BindingBuilder
                .bind(new Queue(queueName))
                .to(new FanoutExchange(exchangeName));

        this.rabbitAdmin.removeBinding(binding);
    }
}