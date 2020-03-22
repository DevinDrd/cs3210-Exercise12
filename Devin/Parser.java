import java.io.IOException;

import java.util.ArrayList;

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
        ArrayList<Node> children = new ArrayList<Node>();
        children.add(parseDef());

        if (!lex.getNextToken().getType().equals("eof")) children.add(parseDefs());

        return new Node("defs", children);
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