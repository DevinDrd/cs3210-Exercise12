import java.util.ArrayList;

public class Node {

    private String type;
    private String content;

    private final ArrayList<Node> children;

    public Node(String type, ArrayList<Node> childs) {
        this.type = type;
        content = "";
        if (childs == null) children = new ArrayList<Node>();
        else children = childs;
    }

    public Node(String type, String content, ArrayList<Node> childs) {
        this.type = type;
        this.content = content;
        if (childs == null) children = new ArrayList<Node>();
        else children = childs;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public String toString() {
        String output = "";

        output += "<" + type;
        if (!content.equals("")) output += "|" + content;
        output += ">";
        
        for (Node n:children) { //     <type|content>
            output += "\r\n    <" + n.getType();
            if (!n.getContent().equals("")) output += "|" + n.getContent();
            output += ">";
        }

        return output;
    }

    public String treeString() {
        String output = "";

        output += nodeString();
        output += treeString(1, children);

        return output;
    }

    private String treeString(int depth, ArrayList<Node> children) {
        String output = "";
        String pre = "";

        for (int i = 1; i < depth; i++) pre += "  | ";

        for (Node n:children) {
            output += pre + "  |-" + n.nodeString();
            output += treeString(depth + 1, n.children);
        }

        return output;
    }

    public String nodeString() {
        String output = "";

        output += "<" + type;
        if (!content.equals("")) output += "|" + content;
        output += ">\r\n";

        return output;
    }

    public static void main(String[] args) {
        Node n1 = new Node("defs", null);
        Node n2 = new Node("def", null);
        Node n3 = new Node("expr", null);
        Node n4 = new Node("defs", null);
        Node n5 = new Node("name", null);

        n1.addChild(n2);
        n1.addChild(n4);
        n2.addChild(n3);
        n3.addChild(n5);

        System.out.println(n1.treeString());
    }
    
}