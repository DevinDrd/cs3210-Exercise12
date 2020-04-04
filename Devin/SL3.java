import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class SL3 {

    private BufferedReader sysIn;

    private Lexar lex;

    private Parser par;

    private Node root;

    private String fileName;

    private SL3() throws IOException {
        sysIn = new BufferedReader(new InputStreamReader(System.in));
    }

    private void run() throws IOException {
        String input;
        Node callNode;

        lex = new Lexar(fileName);
        lex.setVerbos(2);

        par = new Parser(lex);
        root = par.parse();

        System.out.println();
        System.out.println(root.treeString());

        System.out.println();
        System.out.println("Welcome to my SL3 REPL. Enter '(quit)' when finished.");
        lex.setVerbos(0);

        do {
            System.out.print("-->");
            input = sysIn.readLine();

            lex.add(input);
            callNode = par.parseRepl();

            if (callNode.getType().equals("list")) System.out.println(root.call(callNode).string());
            else System.out.println("That wasn't a function call");

        } while (!input.equals("(quit)"));
    }

    public static void main(String[] args) {
        try {
            SL3 sl3 = new SL3();

            if (args.length == 1) {
                sl3.fileName = args[0];
            }
            else {
                System.out.print("Enter SL3 filename: ");
                sl3.fileName = sl3.sysIn.readLine();
            }

            sl3.run();
        } catch (IOException e) {
            System.out.println("|ERROR--->standard input|");
        }// end of try/catch
    } // end main()
}