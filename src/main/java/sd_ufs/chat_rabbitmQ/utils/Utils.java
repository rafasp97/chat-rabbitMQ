package sd_ufs.chat_rabbitmQ.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.google.protobuf.ByteString;
import chat.Content;
import chat.ContentType;
import chat.Message;
public class Utils {

   public static String formatDate(String date, String hour) {
        return "(" + date + " às " + hour + ")";
    }

    public static String formatMessage(Message message) {

        Content content = message.getContent();
        String body = bytesToString(content.getBody());
        String group = message.getGroup();
        
        String base = String.format(
                "%s @%s",
                formatDate(message.getDate(), message.getHour()),
                message.getIssuer()
        );

        if(group.isEmpty()) 
            return String.format("%s diz: %s", base, body);
        else 
            return String.format("%s#%s diz: %s", base, group, body);
    }

    public static Message prepareToSend(String sendBy, String group, String msg) {
        Content content = Content.newBuilder()
            .setType(ContentType.TEXT)
            .setBody(stringToBytes(msg))
            .setName("message")
            .build();

        LocalDateTime now = LocalDateTime.now();

        return Message.newBuilder()
            .setIssuer(sendBy)
            .setGroup(group)
            .setDate(now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .setHour(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
            .setContent(content)
            .build();
    }

    protected static ByteString stringToBytes(String message) {
        if (message == null) return ByteString.EMPTY;
        return ByteString.copyFromUtf8(message);
    }

    protected static String bytesToString(ByteString message) {
        if (message == null || message.isEmpty()) return "";
        return message.toStringUtf8();
    }

}