import java.io.IOException;

public class Parser {

    private Lexar lex;

    public Parser(String fileName) throws IOException {
        lex = new Lexar(fileName);
    }

    public Parser(Lexar lexar) {
        lex = lexar;
    }

    public Node parse() {
        return parseDefs();
    }

    private Node parseDefs() {
        return null;
    }

    private Node parseDef() {
        return null;
    }

    private Node parseParams() {
        return null;
    }

    private Node parseExpr() {
        return null;
    }

    private Node parseList() {
        return null;
    }

    private Node parseItems() {
        return null;
    }

    private void errorCheck(Token token, String type) {

    }

    private void errorCheck(Token token, String type, String content) {

    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {System.out.println("Type: java Parser <filename>"); System.exit(0);}

        Parser parser = new Parser(new Lexar(args[0]));
    }
}