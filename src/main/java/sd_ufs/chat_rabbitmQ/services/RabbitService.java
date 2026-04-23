package sd_ufs.chat_rabbitmQ.services;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueInformation;
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

    public void createQueue(String queueName) {
        QueueInformation info = rabbitAdmin.getQueueInfo(queueName);
        if (info == null) {
            Queue queue = new Queue(queueName, true);
            rabbitAdmin.declareQueue(queue);
        }
    }

    public void sendMessage(String sendBy, String queueName, String msg) {
        String message = String.format(
                "%s @%s, diz: %s",
                Utils.dateNow(),
                sendBy,
                msg
        );
        rabbitTemplate.convertAndSend("", queueName, message);
    }

    public void consumeMessages(String queueName) {
        container.stop();
        container.setQueueNames(queueName);

        //setMessageListener: define uma função que determina o que fazer quando uma mensagem chegar.
        container.setMessageListener(message -> {
            String body = new String(message.getBody());
            System.out.println("\n" + body);
        });

        container.start();
    }
}