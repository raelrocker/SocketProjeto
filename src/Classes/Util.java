package Classes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Util {
    public static Object getObjectFromByte(byte[] objectAsByte) {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
                 bis = new ByteArrayInputStream(objectAsByte);
                 ois = new ObjectInputStream(bis);
                 obj = ois.readObject();

                 bis.close();
                 ois.close();
        } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
        } catch (ClassNotFoundException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();           
        }
        return obj;
    }
}
