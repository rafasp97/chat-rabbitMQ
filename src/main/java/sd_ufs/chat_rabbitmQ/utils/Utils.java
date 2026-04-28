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
}