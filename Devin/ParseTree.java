import java.io.IOException;

public class ParseTree {

    Node root;

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