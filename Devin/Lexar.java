import java.util.Stack;

import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;

public class Lexar {

    private BufferedReader input; // input source
    
    private int nextSym; // stores the next symbol when it was used up from the input source

    private Stack<Token> tokenStack; // holds tokens to be read next, before source is read

    private boolean verbos;  // print lexed tokens when true

    public Lexar() throws IOException {
        nextSym = 0;
        tokenStack = new Stack<Token>();
        verbos = false;
    }

    public Lexar(String fileName) throws IOException {
        input = new BufferedReader(new FileReader(fileName));
        nextSym = 0;
        tokenStack = new Stack<Token>();
        verbos = false;
    }

    public Lexar(BufferedReader source) {
        input = source;
        nextSym = 0;
        tokenStack = new Stack<Token>();
        verbos = false;
    }

    public boolean hasSource() {
        return input != null;
    }

    public boolean hasNext() {
        if (nextSym > 0) return true;
        try {
            return input.ready();
        } catch (IOException e) {
            error("Lexar IOError, can't check for more");
        }
        return false;
    }

    public Token getNextToken() {
        if (!tokenStack.empty()) {
            return tokenStack.pop();
        } else {
            if (!hasNext()) error("Nothing more to lex");
            int state = 1;
            int sym = -1;
            String data = "";
            boolean done = false;
            Token token = null;

            do {
                sym = getNextSym();

                if (!isWhiteSpace(sym) && sym != -1) data += (char) sym; // sym != eof


                if (state == 1) { // starting point
                    if (isWhiteSpace(sym))          state = 1;
                    else if (isLetter(sym))         state = 2;
                    else if(sym == 41 || sym == 40) state = 3; // sym == ) or (
                    else if(sym == 45)              state = 4; // sym == -
                    else if(isDigit(sym))           state = 5;
                    else if (sym == 59)             state = 6; // sym == ;
                    else if (sym == -1)             state = 7;
                    else error("Lexical analysis phase with symbol " + sym + " in state " + state );
                } // end if (state == 1)

                else if (state == 2) { // returns name Token type (or keyword)
                    if (isLetter(sym)) state = 2;
                    else if (isDigit(sym)) state = 2;
                    else if (sym == 41 || sym == 40) { // sym == ) || sym == (
                        data = data.substring(0, data.length() - 1); // peel off excess character
                        putBackSym(sym); // replace excess character
                        token = new Token("name", data);
                    }
                    else if(isWhiteSpace(sym) || sym == -1 || sym == 40) token = new Token("name", data);
                    else error("Lexical analysis phase with symbol " + sym + " in state " + state );
                } // end if (state == 2)

                else if (state == 3) {} // state 3 comes later

                else if (state == 4) { // eventually returns number token type
                    if (isDigit(sym)) state = 5;
                    else error("Lexical analysis phase with symbol " + sym + " in state " + state );
                } // end if (state == 4)

                else if (state == 5) { // eventually returns number token type
                    if (isDigit(sym))   state = 5;
                    else if(sym == 46)  state = 5;
                    else if (sym == 41 || sym == 40) { // sym == ) || sym == (
                        data = data.substring(0, data.length() - 1); // peel off excess character
                        putBackSym(sym); // replace excess character
                        token = new Token("number", data);
                    }
                    else if(isWhiteSpace(sym) || sym == -1) token = new Token("number", data);
                    else error("Lexical analysis phase with symbol " + sym + " in state " + state );
                } // end if (state == 5)
                
                else if (state == 6) { // eventually returns comment token type
                    try{input.readLine();} // dump the rest of the comment line
                    catch(IOException e){}
                    token = new Token("comment", "");
                } // end if (state == 6)

                else if (state == 7) {} // state 7 comes later

                else {
                    error("Unknown state: " + state );
                } // end of else


                if (state == 3) { // returns singleton Token type
                    token = new Token("singleton", data);
                } // end if (state == 3)

                if (state == 7) {
                    token = new Token("eof", "");
                }


            } while (token == null);

            if (verbos()) System.out.println("Lexed--->" + token);
            return token;
        } // end else
    } // end getNextToken()

    public void putBackToken(Token token) {
        tokenStack.push(token);
        System.out.println("Putback token: " + token);
    } // end putBackToken()

    public boolean verbos() {
        return verbos;
    }

    public void setVerbos(boolean moo) {
        verbos = moo;
    }

    public void add(String more) {
        String source = more;
        if (input != null) {
            source = "";
            try {while (input.ready()) source += input.readLine();}
            catch (IOException e) {error("IOException while adding to lexar");}
            source += more;
        }
        input = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(source.getBytes())));
    }

    private int getNextSym() {
        int sym = -1;

        if (nextSym == 0) {
            try {sym = input.read();}
            catch (IOException e) {error("Lexar reading from file");}
        } else {
            sym = nextSym;
            nextSym = 0;
        }

        return sym;
    } // end getNextSym

    private void putBackSym(int sym) {
        if (nextSym == 0) nextSym = sym;
        else error("There is already a next symbol");
    } // end putBackSym

    private static boolean isLetter(int sym) {
        if (sym >= 'A' && sym <= 'Z' || sym >= 'a' && sym <= 'z')
                return true;
        else
                return false;
    } // end isLetter()

    private static boolean isDigit(int sym) {
        if (sym >= '0' && sym <= '9') return true;
        else return false;
    } // end isDigit()

    private static boolean isWhiteSpace(int sym) {
        if (sym == 9 || sym == 10 || sym == 13 || sym == 32)
                return true;
        else
                return false;
    } // end isWhiteSpace()

    private void error(String message) {
        System.out.println("|Error---" + message + "|");
        System.exit(1);
    } // end error()

    public static void main(String[] args) throws Exception {
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        Lexar lex = new Lexar(args[0]);
        lex.setVerbos(true);

        while(lex.hasNext()) lex.getNextToken();

        System.out.println("Enter exit when done.");

        double test = 0.;

        String source;
        do {
            System.out.print("---> ");
            source = sysIn.readLine();
            lex.add(source);
            while (lex.hasNext()) lex.getNextToken();
        } while (!source.equals("exit"));
    } // end main()

} // end Lexar