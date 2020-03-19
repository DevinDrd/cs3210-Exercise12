public class Parser {

    private Lexar lex;

    public Parser(Lexar lexar) {
        lex = lexar;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {System.out.println("Type: java Parser <filename>"); System.exit(0);}

        Parser parser = new Parser(new Lexar(args[0]));
    }
}