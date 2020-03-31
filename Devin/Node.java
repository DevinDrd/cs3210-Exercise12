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

    public void evalDefs(Node callNode) {
        if (!type.equals("defs")) error("Node of type '" + type + "' cannot be evaluated as a defs");
        if (!callNode.type.equals("expr")) error("Node of type '" + callNode.type + "' cannot be evaluated as a expr");

        //               <expr>  <list>      <items>     <expr>      <name>
        String callName = callNode.getChild(0).getChild(0).getChild(0).getChild(0).evalName();
        Node function = getFunction(this, callName);

        if (function != null) function.evalDef(callNode);
        else error("Function named '" + callName + "' was not found");
    }

    private Node getFunction(Node defs, String callName) {
        if (!defs.getType().equals("defs")) error("Node of type '" + defs.type + "' is not of type defs");

        //                          <def>       <name>
        String functionName = defs.getChild(0).getChild(0).evalName();
        if (functionName.equals(callName)) return defs.getChild(0);
        
        if (defs.children().size() <= 1) return null;
        
        return getFunction(defs.getChild(1), callName);
    }

    public Node evalDef(Node call) {
        if (!getType().equals("def")) error("Node of type '" + type + "' is not of type def");

        SDTable params = null;
        Node result = null;
        ArrayList<Double> args = null;

        if (children.size() >= 3) {
            //            <expr>   <list>     <items>    <items>
            args = getArgs(call.getChild(0).getChild(0).getChild(1), new ArrayList<Double>()); // FIXME: An argument could be a call
            params = getChild(1).evalParams();

            if (args.size() != params.size())
                    error("Call " + 
                        call.getChild(0).getChild(0).getChild(0).evalName() + // <expr><list><items><name>
                        " does not have correct # of arguments");

            params.set(args);

            result = getChild(2).evalExpr(params);
        }
        else {
            params = new SDTable();
            result = getChild(1).evalExpr(params);
        }

        return null;
    }

    private ArrayList<Double> getArgs(Node call, ArrayList<Double> args) {
        if (!call.getType().equals("items")) error("Node of type '" + call.type + "' is not of type items");

        args.add(call.getChild(0).evalExpr(new SDTable()).evalNumber());

        if (call.children.size() >= 2) return getArgs(call.getChild(1), args);
        else return args;
    }

    public SDTable evalParams() {
        if (!getType().equals("params")) error("Node of type '" + type + "' is not of type params");
        return getParams(this, new SDTable());
    }

    private SDTable getParams(Node node, SDTable params) {
        if (!node.getType().equals("params")) error("Node of type '" + node.type + "' is not of type params");

        params.put(node.getChild(0).evalName(), 0.0);
        if (node.children.size() >= 2) return getParams(node.getChild(1), params);
        else return params;
    }

    public Node evalExpr(SDTable vars) {
        if (!type.equals("expr")) error("Node of type '" + type + "' cannot be evaluated as an expression");

        System.out.println(vars);
        
        Node child = getChild(0);
        String childType = child.getType();
        Node result = null;

        if (childType.equals("name")) result = child;
        else if (childType.equals("number")) result = child;
        else if (childType.equals("list")) result = child.evalList(vars);
        else error("expr node has unexpected child");

        return result;
    }

    public Node evalList(SDTable vars) {
        
        return null;
    }

    public Node evalItems(SDTable vars) {
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