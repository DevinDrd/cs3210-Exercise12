import java.util.ArrayList;

public class SDTable {
    //String|Node Table

    private ArrayList<String> names;
    private ArrayList<Node> values;

    private int maxNameLength;
    private int maxValueLength;

    public SDTable() {
        names = new ArrayList<String>();
        values = new ArrayList<Node>();
        maxNameLength = 0;
        maxValueLength = 0;
    }

    public SDTable(ArrayList<String> names, ArrayList<Node> values) {
        if (names.size() == values.size()) {
            this.names = names;
            this.values = values;
        } else {
            System.out.println("|Error--->Cannot initialize a SDTable with differing ArrayList sizes|");
        }
    }

    public void put(String name, Node value) {
        int index = find(name);
        if (index < 0) {
            names.add(name);
            values.add(value);
            if (name.length() > maxNameLength) maxNameLength = name.length();
        } else {
            values.set(index, value);
        }
        if (value.toString().length() > maxValueLength)
                maxValueLength = value.toString().length();
    }

    public Node get(String name) {
        int index = find(name);
        if (index >= 0) {
            return values.get(index);
        } else {
            System.out.println("|Error--->variable [" + name + "] not found|");
            System.exit(1);
            return null;
        }
    }

    public void set(String name, Node value) {
        int index = find(name);
        if (index >= 0) {
            values.set(index, value);
        } else {
            System.out.println("|Error--->Key not found|");
            System.exit(1);
        }
    }

    public boolean isEmpty() {
        return names.isEmpty();
    }

    public int size() {
        return names.size();
    }

    public boolean contains(String name) {
        return find(name) >= 0;
    }

    private int find(String name) {
        for (int i = 0; i < names.size(); i++)
                if (names.get(i).equals(name)) return i;
        return -1;
    }

    public String toString() {
        String output = "";
        int delta = 0; // auxilert variable: tracks how many spaces to put

        if (names.size() > 0) {
            output += names.get(0);
            delta = maxNameLength - names.get(0).length();
            for (int j = 0; j < delta; j++) output += " ";
            output += "|" + values.get(0);

            for (int i = 1; i < names.size(); i++) {
                output += "\r\n";
                output += names.get(i);
                delta = maxNameLength - names.get(i).length();
                for (int j = 0; j < delta; j++) output += " ";
                output += "|" + values.get(i);
            }
        }

        return output;
    }

}