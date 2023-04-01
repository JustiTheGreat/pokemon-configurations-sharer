package app.serializators;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializator {
    private Serializator() {
    }

    public static String serialize(Object o) throws IOException {
        String s = "";
        FileOutputStream fileOutputStream = new FileOutputStream(s);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(o);
        objectOutputStream.flush();
        objectOutputStream.close();
        return s;
    }

    public static Object deserialize(String s) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(s);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object o = objectInputStream.readObject();
        objectInputStream.close();
        return o;
    }
}
