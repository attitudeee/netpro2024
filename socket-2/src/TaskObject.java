import java.io.Serializable;

public class TaskObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    private String content;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
