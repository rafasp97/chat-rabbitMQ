package sd_ufs.chat_rabbitmQ.enums;

public enum ActionType {
    SEND,
    COMMAND,
    MESSAGE;

    public static ActionType defineAction(String input) {
        if (input.contains("@") || input.contains("#")) return ActionType.SEND;
        if (input.contains("!")) return ActionType.COMMAND;
        return ActionType.MESSAGE; 
    }
}
