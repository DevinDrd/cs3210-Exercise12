public class Token {
    private final String type;
    private final String content;

    public Token(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String toString() {
        return content;
    }
}