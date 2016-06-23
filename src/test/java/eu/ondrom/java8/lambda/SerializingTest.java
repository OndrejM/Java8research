package eu.ondrom.java8.lambda;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import static org.junit.Assert.*;
import org.junit.Test;

public class SerializingTest {
    
    public static interface SerializableRunnable extends Runnable, Serializable {
    }
    
    @Test
    public void serializableLambdaCanBeSerialized() throws IOException, ClassNotFoundException {
        Runnable r = (Runnable & Serializable)() -> System.out.println("Serializable!");
        givenLambdaCanBeSerialized(r);
    }

    @Test(expected = NotSerializableException.class)
    public void anyLambdaCannotBeSerialized() throws IOException, ClassNotFoundException {
        Runnable r = () -> System.out.println("Serializable!");
        givenLambdaCanBeSerialized(r);
    }

    @Test(expected = ClassCastException.class)
    public void anyLambdaCannotBeEasilyMadeSerializable() throws IOException, ClassNotFoundException {
        Runnable r = () -> System.out.println("Serializable!");
        Runnable sr = (Runnable & Serializable)r;
        givenLambdaCanBeSerialized(sr);
    }

    @Test
    public void anyLambdaExprCanBeMadeSerializable() throws IOException, ClassNotFoundException {
        givenSerializableLambdaCanBeSerialized(() -> System.out.println("Serializable!"));
    }
    
    private void givenSerializableLambdaCanBeSerialized(SerializableRunnable r) throws ClassNotFoundException, IOException {
        givenLambdaCanBeSerialized(r);
    }
    
    private void givenLambdaCanBeSerialized(Runnable r) throws ClassNotFoundException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(r);
        Object o = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())).readObject();
        assertNotNull(o);
        assertEquals(r.getClass().getPackage(), o.getClass().getPackage());
    }
    
    public static void main(String[] args) throws Exception {
        new SerializingTest().serializableLambdaCanBeSerialized();
    }
}
