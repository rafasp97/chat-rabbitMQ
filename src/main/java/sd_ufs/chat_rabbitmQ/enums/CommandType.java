package sd_ufs.chat_rabbitmQ.enums;

public enum CommandType {
    ADDGROUP,
    REMOVEGROUP,
    ADDUSER,
    REMOVEUSER,
    UNKNOWN;

    public static CommandType defineCommand(String command) {
        try {
            return CommandType.valueOf(command.toUpperCase());
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
