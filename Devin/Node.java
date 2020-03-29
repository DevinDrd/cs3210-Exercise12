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

    public Node(Token token) {
        type = token.getType();
        content = token.getContent();
        children = new ArrayList<Node>();
    }

    public Double evalDefs(Node callNode) {
        if (!type.equals("defs")) error("Node of type '" + type + "' cannot be evaluated as a defs");

        //               <expr>  <list>      <items>     <expr>      <name>
        String callName = callNode.getChild(0).getChild(0).getChild(0).getChild(0).evalName();
        Node function = getFunction(this, callName);

        System.out.println(function.treeString());

        return function.evalDef(callNode);
    }

    private Node getFunction(Node defs, String callName) {
        if (!defs.getType().equals("defs")) error("Node of type '" + type + "' is not of type defs");

        //                          <def>       <name>
        String functionName = defs.getChild(0).getChild(0).evalName();
        if (functionName.equals(callName)) return defs.getChild(0);
        
        if (defs.children().size() <= 1) error("Function named '" + functionName + "' was not found");
        
        return getFunction(defs.getChild(1), callName);
    }

    public Double evalDef(Node call) {
        return null;
    }

    public String evalParams() {
        return null;
    }

    public Node evalExpr() {
        if (!type.equals("expr")) error("Node of type '" + type + "' cannot be evaluated as an expression");
        return children.get(0);
    }

    public Node evalList() {
        return null;
    }

    public Node evalItems() {
        return null;
    }

    public String evalName() {
        if (!type.equals("name")) error("Node of type '" + type + "' cannot be evaluated as a name");
        return content;
    }

    public Double evalNumber() {
        if (!type.equals("number")) error("Node of type '" + type + "' cannot be evaluated as a number");
        return Double.parseDouble(content);
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

    public ArrayList<Node> children() {
        return children;
    }

    public Node getChild(int i) {
        return children.get(i);
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

        output += "\r\n" + pre;
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
        output += ">";

        return output;
    }

    private void error(String message) {
        System.out.println("|Error---" + message + "|");
        System.exit(1);
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