import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class SL3 {

    private BufferedReader sysIn;

    public static Hashmap functionDefs;
    public static Hashmap varDefs;

    private SL3() throws IOException {
        sysIn = new BufferedReader(new InputStreamReader(System.in));
    }

    private void start() {
        // tell parser to build tree
        // start REPL
        // close down
    }

    public static void main(String[] args) {
        try {
            new SL3().start();
        } catch (IOException e) {
            System.out.println("|ERROR--->standard input|");
        }
    }
}