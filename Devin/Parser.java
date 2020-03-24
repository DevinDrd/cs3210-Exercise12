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
        ArrayList<Node> children = new ArrayList<Node>();
        Token token = lex.getNextToken();

        errorCheck(token, "singleton", "(");

        children.add(parseName());

        token = lex.getNextToken();
        errorCheck(token, "singleton", "(");

        children.add(parseName());

        token = lex.getNextToken();

        if (!token.equals(new Token("singleton", ")"))) {
            lex.putBackToken(token);
            children.add(parseParams());
            token = lex.getNextToken();
        }

        // System.out.println("Here");
        errorCheck(token, "singleton", ")");

        children.add(parseExpr());

        token = lex.getNextToken();
        errorCheck(token, "singleton", ")");

        return new Node("def", children);
    }

    private Node parseParams() {
        ArrayList<Node> children = new ArrayList<Node>();
        Token token;

        children.add(parseName());

        token = lex.getNextToken();
        if (token.getType().equals("name")) {
            lex.putBackToken(token);
            children.add(parseParams());
        }
        else lex.putBackToken(token);
        
        return new Node("params", children);
    }

    private Node parseExpr() {
        ArrayList<Node> children = new ArrayList<Node>();
        Token token = lex.getNextToken();
        
        if (token.getType().equals("name")) {
            lex.putBackToken(token);
            children.add(parseName());
        }
        else if (token.getType().equals("number")) {
            lex.putBackToken(token);
            children.add(parseNumber());
        }
        else {
            lex.putBackToken(token);
            children.add(parseList());
        }

        return new Node("expr", children);
    }

    private Node parseList() {
        return null;
    }

    private Node parseItems() {
        return null;
    }

    private Node parseName() {
        Token token = lex.getNextToken();
        errorCheck(token, "name");
        return new Node(token);
    }

    private Node parseNumber() {
        Token token = lex.getNextToken();
        errorCheck(token, "number");
        return new Node(token);
    }

    private void errorCheck(Token token, String type) {
        if (token.getType() != type) error("Token " + token + " is not of type " + type);
    }

    private void errorCheck(Token token, String type, String content) {
        Token token2 = new Token(type, content);
        if (!token.equals(token2)) error("Token " + token + " does not equal " + token2);
    }

    private void error(String message) {
        System.out.println("|Error---" + message + "|");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {System.out.println("Type: java Parser <filename>"); System.exit(0);}

        Lexar lexar = new Lexar(args[0]);
        lexar.setVerbos(2);
        Parser parser = new Parser(lexar);
        Node root = parser.parseDefs();
        System.out.print(root.treeString());
    }
}