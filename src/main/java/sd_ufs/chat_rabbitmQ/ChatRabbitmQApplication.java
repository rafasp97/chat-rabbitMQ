package sd_ufs.chat_rabbitmQ;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import sd_ufs.chat_rabbitmQ.services.ChatService;

@EnableAsync
@SpringBootApplication
public class ChatRabbitmQApplication implements CommandLineRunner {

    private final ChatService chatService;

    public ChatRabbitmQApplication(ChatService chatService) {
        this.chatService = chatService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatRabbitmQApplication.class, args);
    }

    @Override
    public void run(String... args) {

        chatService.start();
        chatService.processActions();
    }
}