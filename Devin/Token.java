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
        return "[" + type + ", " + content + "]";
    }

    public static void main(String[] args) {
        Token t1 = new Token("", "");
        Token t2 = new Token("", "");

        System.out.println(t1.equals(t2));
    }
}