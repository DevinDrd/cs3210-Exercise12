import java.io.IOException;

public class ParseTree {

    private Node root;

    public static SDTable functionDefs;
    public static SDTable varDefs;

    public ParseTree(String fileName) throws IOException {
        Parser parser = new Parser(fileName);
        root = parser.parse();
    }

    public ParseTree(Parser parser) {
        root = parser.parse();
    }

    public ParseTree(Node node) {
        root = node;
    }
}