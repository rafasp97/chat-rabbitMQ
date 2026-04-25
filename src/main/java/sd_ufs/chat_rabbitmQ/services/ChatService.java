package sd_ufs.chat_rabbitmQ.services;

import org.springframework.stereotype.Service;
import java.util.Scanner;

@Service
public class ChatService {
    private final RabbitService rabbitService;
    private final Scanner scanner = new Scanner(System.in);
    private String user;
    private String sendTo = "";

    public ChatService(RabbitService rabbitService) {
        this.rabbitService = rabbitService;
    }

    public void start() {
        this.accessUser();
    }

    public void processActions() {
        this.determinePrefix();
        String input = scanner.nextLine();

        if(input.contains("@")) {
            this.setSendTo(input.replace("@", ""));
        } else {
            rabbitService.sendMessage(this.user, this.sendTo, input);
        }

        this.processActions();
    }

    public void determinePrefix() {
        if(!this.sendTo.isEmpty()){
            System.out.print("@" + this.sendTo + "<< ");
        } else {
            System.out.print("<< ");
        } 
    }

    private void accessUser() {
        System.out.print("User: ");
        String input = scanner.nextLine();
        setUser(input, this::accessUser);
        rabbitService.consumeMessages(this.user, this::determinePrefix);
    }

    private void setUser(String user, Runnable method) {
        if (!user.isEmpty()) {
            this.user = user;
            rabbitService.createQueue(this.user);
            return;
        };
        System.out.println("Nome de usuário inválido...");
        method.run();
    }

    private void setSendTo(String sendTo) {
        if (!sendTo.isEmpty()) {
            this.sendTo = sendTo;
            rabbitService.createQueue(this.sendTo);
            return;
        };
        this.sendTo = "";
        System.out.println("Nome de usuário inválido...");
        
    }

}
