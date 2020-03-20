import java.util.ArrayList;

public class SDTable {
    //String Double Table

    private ArrayList<String> names;
    private ArrayList<Double> values;

    public SDTable() {
        names = new ArrayList<String>();
        values = new ArrayList<Double>();
    }

    public void put(String name, Double value) {
        int index = find(name);
        if (index < 0) {
            names.add(name);
            values.add(value);
        } else {
            values.set(index, value);
        }
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

}