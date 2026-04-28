package sd_ufs.chat_rabbitmQ.services;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Service;
import sd_ufs.chat_rabbitmQ.utils.Utils;

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

    public void sendMessage(String sendBy, String sendTo, String msg, char prefix) {
        String message = Utils.formatMessage(sendBy, sendTo, msg, prefix);

        if(prefix == '@') this.rabbitTemplate.convertAndSend("", sendTo, message);
        if(prefix == '#') this.rabbitTemplate.convertAndSend(sendTo, "", message);
    }

    public void consumeMessages(String queueName, Runnable determinePrefix) {
        this.container.stop();
        this.container.setQueueNames(queueName);
        //setMessageListener: define uma função que determina o que fazer quando uma mensagem chegar.
        this.container.setMessageListener(message -> {
            String body = new String(message.getBody());
            System.out.println("\n" + body);

            determinePrefix.run();
        });
        this.container.start();
    }

    public void createExchange(String exchangeName, String queueName) {
        FanoutExchange exchange = new FanoutExchange(exchangeName);
        this.rabbitAdmin.declareExchange(exchange);
        this.bindQueueToExchange(exchangeName, queueName);
    }

    public void bindQueueToExchange(String exchangeName, String queueName) {
        this.createQueue(queueName);
        Binding binding = BindingBuilder
                .bind(new Queue(queueName))
                .to(new FanoutExchange(exchangeName));
        //O 'new Queue' não cria uma nova fila, apenas faz referência a uma fila existente (objeto Queue);
        //O rabbit depende do this.rabbitAdmin para fazer alterações no servidor rabbit.
        this.rabbitAdmin.declareBinding(binding);
    }
}