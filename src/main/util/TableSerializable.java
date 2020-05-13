package main.util;

import main.logic.State;
import main.table.Table;

import java.io.*;

public class TableSerializable {
    public static void serialize(Table table, String path) throws IOException {
        FileOutputStream fout = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(table);
    }
    public static Table deserialize(String path, byte[] data) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream is = new ObjectInputStream(fis);
        return (Table) is.readObject();
    }
}
