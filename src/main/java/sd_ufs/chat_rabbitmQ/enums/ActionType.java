package sd_ufs.chat_rabbitmQ.enums;

public enum ActionType {
    SEND,
    COMMAND,
    MESSAGE;

    public static ActionType defineAction(String input) {
        if (input.startsWith("@") || input.startsWith("#")) return ActionType.SEND;
        if (input.startsWith("!")) return ActionType.COMMAND;
        else return ActionType.MESSAGE; 
    }
}
