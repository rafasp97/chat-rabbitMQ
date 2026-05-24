package sd_ufs.chat_rabbitmQ.model;

import java.io.File;
import sd_ufs.chat_rabbitmQ.enums.BodyMessageType;

public class BodyMessage {

    private BodyMessageType type;
    private String text;
    private File file;

    public static BodyMessage text(String text) {
        BodyMessage b = new BodyMessage();
        b.type = BodyMessageType.TEXT;
        b.text = text;
        return b;
    }

    public static BodyMessage file(File file) {
        BodyMessage b = new BodyMessage();
        b.type = BodyMessageType.FILE;
        b.file = file;
        return b;
    }

    public boolean isText() {
        return type == BodyMessageType.TEXT;
    }

    public boolean isFile() {
        return type == BodyMessageType.FILE;
    }

    public String getText() {
        return text;
    }

    public File getFile() {
        return file;
    }

    public BodyMessageType getType() {
        return type;
    }
}