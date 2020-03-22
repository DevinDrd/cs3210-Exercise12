import java.util.ArrayList;

public class Node {

    private String type;

    private ArrayList<Node> children;

    public Node(String type, ArrayList<Node> children) {
        this.type = type;
        this.children = children;
    }
    
}