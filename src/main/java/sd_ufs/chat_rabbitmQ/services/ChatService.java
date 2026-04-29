package sd_ufs.chat_rabbitmQ.services;

import org.springframework.stereotype.Service;
import sd_ufs.chat_rabbitmQ.enums.ActionType;
import sd_ufs.chat_rabbitmQ.enums.CommandType;

import java.util.Scanner;

@Service
public class ChatService {
    private final RabbitService rabbitService;
    private final Scanner scanner = new Scanner(System.in);
    private String user;
    private String sendTo = "";
    private char prefix;

    public ChatService(RabbitService rabbitService) {
        this.rabbitService = rabbitService;
    }

    public void start() {
        this.accessUser();
    }

    public void processActions() {
        this.determinePrefix();

        String input = scanner.nextLine();

        switch (ActionType.defineAction(input)) {
            case SEND -> this.setSendTo(input.replaceFirst("^[#@!]", ""), input.charAt(0));
            case COMMAND -> this.activateCommand(input.replaceFirst("^[#@!]", ""));
            case MESSAGE -> this.rabbitService.sendMessage(this.user, this.sendTo, input, this.prefix);
        }

        this.processActions();
    }

    public void determinePrefix() {
        if (this.sendTo != null && !this.sendTo.isEmpty()) {
            String prefix = this.rabbitService.queueExists(sendTo) ? "@" : "#";
            //TO DO: quando o grupo não existir e ele usar um prefixo #, vai começar a usar e vai da eror na hr de mandar msg.
            System.out.print(prefix + this.sendTo + "<< ");
        } else {
            System.out.print("<< ");
        }
    }

    private void accessUser() {
        System.out.print("User: ");
        String input = scanner.nextLine();
        setUser(input, this::accessUser);
        this.rabbitService.consumeMessages(this.user, this::determinePrefix);
    }

    private void activateCommand(String input) {
        String[] parts = input.trim().split("\\s+");
        String command = parts[0];

        switch (CommandType.defineCommand(command)) {
            case ADDGROUP -> this.addGroupCommand(parts);
            case REMOVEGROUP -> this.removeGroupCommand(parts);
            case ADDUSER -> this.addUserCommand(parts);
            case REMOVEUSER -> this.removeUserByGroupCommand(parts);
            case UNKNOWN -> System.out.println("Unknown command: " + command);
        }
    }

    private void addGroupCommand(String[] parts){
        if (parts.length != 2) {
            System.out.println("Group not selected");
            return;
        }

        String group = parts[1];

        this.rabbitService.createExchange(group, this.user);
    }

    private void removeGroupCommand(String[] parts){
        if (parts.length != 2) {
            System.out.println("Group not selected");
            return;
        }

        String group = parts[1];

        if(!this.rabbitService.exchangeExists(group)) {
            System.out.println("Group not found");
            return;
        }

        this.rabbitService.deleteExchange(group);
    }

    private void addUserCommand(String[] parts){
        if (parts.length != 3) {
            System.out.println("Group or User not selected");
            return;
        }

        String user = parts[1];
        String group = parts[2];

        if(!this.rabbitService.queueExists(user) || !this.rabbitService.exchangeExists(group)) {
            System.out.println("Group or User not found");
            return;
        }

        this.rabbitService.bindQueueToExchange(group, user);
    }

    private void removeUserByGroupCommand(String[] parts){
        if (parts.length != 3) {
            System.out.println("Group or User not selected");
            return;
        }

        String user = parts[1];
        String group = parts[2];

        if(!this.rabbitService.queueExists(user) || !this.rabbitService.exchangeExists(group)) {
            System.out.println("Group or User not found");
            return;
        }

        this.rabbitService.unbindQueueFromExchange(group, user);

    }

    private void setUser(String user, Runnable method) {
        if (!user.isEmpty()) {
            this.user = user;
            this.rabbitService.createQueue(this.user);
            return;
        };
        System.out.println("Invalid username...");
        method.run();
    }

    private void setSendTo(String sendTo, char prefix) {
        if (sendTo == null || sendTo.isBlank()) {
            this.sendTo = "";
            System.out.println("Invalid username...");
            return;
        }

        this.sendTo = sendTo;
        this.prefix = prefix;

        if (this.prefix == '@') this.rabbitService.createQueue(this.sendTo);

        if (this.prefix == '#' &&  !this.rabbitService.exchangeExists(sendTo))  {
            System.out.println("Group not found");
            this.sendTo = "";
        }
    }

}
