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

    public boolean isType(String type) {
        return this.type == type;
    }

    public boolean equals(Token token) {
        return type == token.getType() && content == token.getContent();
    }

    public String toString() {
        return content;
    }
}