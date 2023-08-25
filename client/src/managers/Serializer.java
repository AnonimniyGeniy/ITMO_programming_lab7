package managers;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * Class that converts objects to byte arrays using stream chains
 */
public class Serializer {
    ObjectOutputStream objectOutputStream;
    ByteArrayOutputStream byteArrayOutputStream;

    public Serializer() {
    }

    public ByteBuffer serialize(Object object) {
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
