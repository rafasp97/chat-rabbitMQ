package sd_ufs.chat_rabbitmQ.utils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.google.protobuf.ByteString;
import chat.Content;
import chat.Message;
import sd_ufs.chat_rabbitmQ.model.BodyMessage;

public class Utils {

   public static String formatDate(String date, String hour) {
        return "(" + date + " at " + hour + ")";
    }

    public static String formatMessage(Message message, String sendTo) {

        Content content = message.getContent();
        String group = message.getGroup();
        String issuer = message.getIssuer();
        String date = message.getDate();
        String hour = message.getHour();
        String name = content.getName();
        String body = bytesToString(content.getBody());
        String type = content.getType();
        
        String base = String.format(
                "%s @%s",
                formatDate(message.getDate(), message.getHour()),
                message.getIssuer()
        );

        if(!"text/plain".equals(type)){
            downloadFile(content.getBody().toByteArray(), name, issuer, sendTo, hour, date, group);

            String groupPart = (group != null && !group.isBlank())
                    ? " by group " + group
                    : "";

            return  String.format("%s File '%s' received from %s%s !", formatDate(date, hour), name, issuer, groupPart);
        } 
        else if(group.isEmpty()) 
            return String.format("%s diz: %s", base, body);
        else 
            return String.format("%s#%s diz: %s", base, group, body);
    }

    public static Message prepareToSend(String sendBy, String group, BodyMessage msg) {

        Content content = prepareContent(msg);
        LocalDateTime now = LocalDateTime.now();

        return Message.newBuilder()
            .setIssuer(sendBy)
            .setGroup(group)
            .setDate(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .setHour(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
            .setContent(content)
            .build();
    }

    private static Content prepareContent(BodyMessage msg) {
        String name;
        String type;
        ByteString body;

        if (msg.isText()) {
            name = "text message";
            type = "text/plain";
            body = stringToBytes(msg.getText());
        } else {
            File file = msg.getFile();
            name = file.getName();
            type = getMimeType(file);
            body = fileToBytes(file);
        }

        return Content.newBuilder()
                .setName(name)
                .setType(type)
                .setBody(body)
                .build();
    }

    private static ByteString stringToBytes(String message) {
        if (message == null) return ByteString.EMPTY;
        return ByteString.copyFromUtf8(message);
    }

    private static ByteString fileToBytes(File file) {
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return ByteString.copyFrom(bytes);
        } catch (Exception e) {
            System.out.println("The file could not be processed");
            return ByteString.EMPTY;
        }
    }

    private static String getMimeType(File file) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    private static String bytesToString(ByteString message) {
        if (message == null || message.isEmpty()) return "";
        return message.toStringUtf8();
    }

    public static File findFile(String path){
        File file = new File(System.getProperty("user.dir"), path);
        if (!file.exists() || !file.isFile()) {
            System.out.println("File not found");
            return null;
        }

        return file;
    }

    private static void downloadFile(byte[] data, String fileName, String issuer, String sendTo, String hour, String date, String group) {

        String FileNameFormated = buildFileName(fileName, issuer, sendTo, hour, date, group);

        try {
            Path path = Paths.get(
                    System.getProperty("user.dir"),
                    "downloads",
                    FileNameFormated
            );
            Files.createDirectories(path.getParent());
            Files.write(path, data);
        } catch (IOException e) {
            System.out.println("Failed to save file: ");
        }
    }

    private static String buildFileName(String fileName, String issuer, String sendTo, String hour, String date, String group) {
        return "from-" + issuer +
                (!group.isBlank() ? "-by-" + group : "") +
                "_to-" + sendTo +
                "_date-" + sanitize(date) +
                "_time-" + sanitize(hour) +
                "_" + sanitize(fileName);
    }

    private static String sanitize(String input) {
        return input == null
                ? "unknown"
                : input.replaceAll("[/\\\\:*?\"<>|]", "-");
    }

}