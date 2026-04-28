package sd_ufs.chat_rabbitmQ.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Utils {

   public static String dateNow() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

        LocalDateTime now = LocalDateTime.now();

        return "(" + now.format(formatter) + ")";
    }

    public static String formatMessage(String sendBy, String sendTo, String msg, char prefix) {
        String base = String.format(
                "%s @%s",
                dateNow(),
                sendBy
        );

        return switch (prefix) {
            case '@' -> String.format("%s diz: %s", base, msg);
            case '#' -> String.format("%s#%s diz: %s", base, sendTo, msg);
            default -> throw new IllegalArgumentException("Invalid Prefix: " + prefix);
        };
    }

}