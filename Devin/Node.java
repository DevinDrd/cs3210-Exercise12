import java.util.ArrayList;
import java.util.Stack;

public class Node {

    private String type;
    private String content;

    private ArrayList<Node> children;

    private static Stack<SDTable> memStack = new Stack<SDTable>(); // stack of tables holding variables for each function call
    private static SDTable table = new SDTable();                  // auxilery reference to the top of the stack

    private static Node root;

    public Node(String type, ArrayList<Node> childs) {
        this.type = type;
        content = "";
        if (childs == null) children = new ArrayList<Node>();
        else children = childs;

        if (type.equals("defs")) root = this;
    }

    public Node(String type) {
        this.type = type;
        this.content = "";
        children = new ArrayList<Node>();

        if (root == null) root = this;
    }

    public Node(String type, String content) {
        this.type = type;
        this.content = content;
        children = new ArrayList<Node>();

        if (root == null) root = this;
    }

    public Node(String type, String content, ArrayList<Node> childs) {
        this.type = type;
        this.content = content;
        if (childs == null) children = new ArrayList<Node>();
        else children = childs;

        if (root == null) root = this;
    }

    public Node (String type, Node child) {
        this.type = type;
        content = "";
        children = new ArrayList<Node>();
        children.add(child);

        if (root == null) root = this;
    }

    public Node(Token token) {
        type = token.getType();
        content = token.getContent();
        children = new ArrayList<Node>();

        memStack = new Stack<SDTable>();
        table = new SDTable();

        if (root == null) root = this;
    }

    public Node call(Node callNode) {
        if (!type.equals("defs")) error("Can only execute node of type defs, not '" + type + "'");
        if (!callNode.type.equals("list")) error("Node of type '" + callNode.type + "' cannot call a function");

        Node result = null;

        if (callNode.children.size() > 0) {
            //               <list>   <items>     <expr>      <name>
            Node callName = callNode.getChild(0).getChild(0).getChild(0);

            if (callName.type.equals("name")) {
                Node function = getFunction(this, callName.evalName());

                //                              <list>
                if (function == null) result = callNode.preDef();   
                else result = function.callDef(callNode);

                if (result == null) error("Function named '" + callName.evalName() + "' was not found");
            }
            else System.out.println("That's not a function call");
        }
        else System.out.println("That's an empty function call");

        return result;
    }

    public Node callDef(Node callNode) {
        if (!getType().equals("def")) error("Node of type '" + type + "' is not of type def");

        Node result = null;
        ArrayList<String> params = null;
        ArrayList<Node> args = null;

        //   <list>   <items>
        if (callNode.getChild(0).children.size() >= 2)
                args = getArgs(callNode.getChild(0).getChild(1), new ArrayList<Node>());
        //                      <list>    <items>     <items>
        else args = new ArrayList<Node>();

        if (children.size() >= 3) {
            params = getParams(getChild(1), new ArrayList<String>());

            if (args.size() != params.size())
                    error("Call " + 
                        callNode.getChild(0).getChild(0).getChild(0).evalName() + // <list><items><expr><name>
                        " does not have correct # of arguments");

            memStack.push(new SDTable(params, args));
            result = getChild(2).evaluate();
        }
        else {
            params = new ArrayList<String>();
            if (args.size() != params.size())
                    error("Call " + 
                        callNode.getChild(0).getChild(0).getChild(0).evalName() + // <list><items><expr><name>
                        " does not have correct # of arguments");

            table = new SDTable(params, args);
            memStack.push(table);
            result = getChild(1).evaluate();
        }

        memStack.pop();

        return result;
    }

    private Node getFunction(Node defs, String callName) {
        if (!defs.getType().equals("defs")) error("Node of type '" + defs.type + "' is not of type defs");

        //                          <def>       <name>
        String functionName = defs.getChild(0).getChild(0).evalName();
        if (functionName.equals(callName)) return defs.getChild(0);
        
        if (defs.children().size() <= 1) return null;
        
        return getFunction(defs.getChild(1), callName);
    }

    private ArrayList<String> getParams(Node node, ArrayList<String> params) {
        if (!node.getType().equals("params")) error("Node of type '" + node.type + "' is not of type params");

        params.add(node.getChild(0).evalName());
        if (node.children.size() >= 2) return getParams(node.getChild(1), params);
        else return params;
    }

    private ArrayList<Node> getArgs(Node node, ArrayList<Node> args) {
        if (!node.getType().equals("items")) error("Node of type '" + node.type + "' is not of type items");

        args.add(node.getChild(0).evaluate());
        if (node.children.size() >= 2) return getArgs(node.getChild(1), args);
        else return args;
    }

    public Node evaluate() {
        Node result = null;

        if (type.equals("expr")) {
            result = getChild(0).evaluate();
        }
        else if (type.equals("list")) {
            //   <items>      <expr>      <name>?
            if (getChild(0).getChild(0).getChild(0).type.equals("name")) result = root.call(this);
            else result = this;
        }
        else if (type.equals("items")) {
            return this;
        }
        else if (type.equals("name")) {
            table = memStack.peek();
            Double value = null;
            
            if (table.contains(evalName())) result = table.get(evalName());
            else error("No variable found with name '" + evalName() + "' in this function");
        }
        else if (type.equals("number")) {
            result = this;
        }
        else {
            error("Cannot evaluate node of type '" + type + "'");
        }

        return result;
    }

    public Node preDef() {
        if (!type.equals("list")) error("Can only call with a node of type list, not '" + type + "'");

        Node result = null;

        //                 <items>     <expr>      <name>
        String callName = getChild(0).getChild(0).getChild(0).evalName();
        
        //         <items>
        Node n = getChild(0); // auxilery node
        ArrayList<Node> args = new ArrayList<Node>();
        while (n.children.size() == 2) {
            n = n.getChild(1);
            args.add(n.getChild(0));
        }

        if (args.size() == 0) {
            if (callName.equals("read")) {
                
            }
            else if (callName.equals("nl")) {
            
            }
            else if (callName.equals("quit")) {
                result = new Node("quit");
            }
        }


        else if (args.size() == 1) {
            if (callName.equals("not")) {
                
            }
            else if (callName.equals("first")) {
                
            }
            else if (callName.equals("rest")) {
                
            }
            else if (callName.equals("null")) {
                
            }
            else if (callName.equals("num")) {
                
            }
            else if (callName.equals("list")) {
                
            }
            else if (callName.equals("write")) {
                System.out.print(args.get(0).evaluate().string() + " ");
                result = new Node("noop");
            }
            else if (callName.equals("quote")) {
                
            }
        }


        else if (args.size() == 2) {
            if (callName.equals("plus")) {
                Double sum = args.get(0).evaluate().evalNumber() + args.get(1).evaluate().evalNumber();
                result = new Node("number", sum.toString());
            }
            else if (callName.equals("minus")) {
                Double diff = args.get(0).evaluate().evalNumber() - args.get(1).evaluate().evalNumber();
                result = new Node("number", diff.toString());
            }
            else if (callName.equals("times")) {
                
            }
            else if (callName.equals("div")) {
                
            }
            else if (callName.equals("lt")) {
                
            }
            else if (callName.equals("le")) {
                
            }
            else if (callName.equals("eq")) {
                Double opandone = args.get(0).evaluate().evalNumber();
                Double opandtwo = args.get(1).evaluate().evalNumber();

                if (opandone.equals(opandtwo)) {
                    result = new Node("number", "1");
                } else {
                    result = new Node("number", "0");
                }
            }
            else if (callName.equals("ne")) {
                
            }
            else if (callName.equals("and")) {
                
            }
            else if (callName.equals("or")) {
                Double opandone = args.get(0).evaluate().evalNumber();
                Double opandtwo = args.get(1).evaluate().evalNumber();

                if (opandone != 0 || opandtwo != 0) {
                    result = new Node("number", "1");
                } else {
                    result = new Node("number", "0");
                }
            }
            else if (callName.equals("ins")) {
                
            }
        }


        else if (args.size() == 3) {
            if (callName.equals("if")) {
                Double condition = args.get(0).evaluate().evalNumber();

                if (condition != 0) {
                    result = args.get(1).evaluate();
                } else {
                    result = args.get(2).evaluate();
                }
            }
        }


        return result;
    }

    private String evalName() {
        if (!type.equals("name")) error("Cannot evaluate node of type '" + type + "' as a name");
        return content;
    }

    private Double evalNumber() {
        if (!type.equals("number")) error("Cannot evaluate node of type '" + type + "' as a number");
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

    public String string() {
        String output = "";
        
        if (type.equals("number")) {
            output += evalNumber().toString();
        } else if (type.equals("list")) {
            output += "(";

            //                                  <items>      <expr>
            if (children.size() > 0) output += getChild(0).getChild(0).evaluate().string();
            //        <items>
            Node n = getChild(0);

            while (n.children.size() == 2) {
                n = n.getChild(1);
                output += " " + n.getChild(0).evaluate().string();
            }

            output += ")";
        } else {
            output += toString();
        }
        
        return output;
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
        System.out.println("|Error--->" + message + "|");
        System.exit(1);
    }

}