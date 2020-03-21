import java.util.ArrayList;

public class SDTable {
    //String|Double Table

    private ArrayList<String> names;
    private ArrayList<Double> values;

    private int maxNameLength;
    private int maxValueLength;

    public SDTable() {
        names = new ArrayList<String>();
        values = new ArrayList<Double>();
        maxNameLength = 0;
        maxValueLength = 0;
    }

    public void put(String name, Double value) {
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

    public double get(String name) {
        int index = find(name);
        if (index >= 0) {
            return values.get(index);
        } else {
            System.out.println("|Error--->variable [" + name + "] not found|");
            System.exit(1);
            return -1;
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
        /*
        String name = "name";
        String value = "value";


        output += name;
        delta = maxNameLength - name.length();
        for (int i = 0; i < delta; i++) output += " ";

        output += "|" + value;
        */
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
        return output;
    }

    public static void main(String[] args) {
        /*
        SDTable table = new SDTable();
        System.out.println(table.size());
        System.out.println(table.isEmpty());
        System.out.println(table.contains("devin"));
        table.put("devin", 0.0);
        table.put("devin", 1.0);
        table.put("darnell", 2.0);
        System.out.println(table.size());
        System.out.println(table.isEmpty());
        System.out.println(table.contains("devin"));
        System.out.println(table.get("devin"));
        System.out.println(table.get("darnell"));
        */
        SDTable table = new SDTable();
        table.put("devin", 0.0);
        table.put("darnell", 3.14159);
        System.out.println(table);

    }

}