import java.util.ArrayList;

public class Node {

    private String type;
    private String content;

    private final ArrayList<Node> children;

    public static SDTable functionDefs;
    public static SDTable varDefs;

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

    public Node(Token token) {
        type = token.getType();
        content = token.getContent();
        children = new ArrayList<Node>();
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

    private boolean isLeaf() {
        return children.size() <= 0;
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

        for (int i = 0; i < children.size(); i++)
                output += treeString(children.get(i), "  ", i != children.size() - 1);

        return output;
    }

    private String treeString(Node n, String pre, boolean more) {
        String output = "";

        output += pre;
        output += (more? "├─":"└─"); // alternative |—, ――
        output += n.nodeString();

        pre += (more? "│   ":"    "); // alternative |

        for (int i = 0; i < n.children.size(); i++)
                output += treeString(n.children.get(i), pre, i != n.children.size() - 1);

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

        System.out.print(n1.treeString());
    }
    
}