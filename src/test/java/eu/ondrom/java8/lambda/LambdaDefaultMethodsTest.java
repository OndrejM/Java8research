package eu.ondrom.java8.lambda;

import java.util.function.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ondro
 */
public class LambdaDefaultMethodsTest {
    
    public LambdaDefaultMethodsTest() {
    }

    @Test
    public void hello() {
        BiFunction<String, String, String> operator = (a,b) -> {
            return a + b;
        };
        operator = operator.andThen(x -> x + "!");
        String result = runFunction(operator);
        assertEquals("After operator applied", "AB", result);
    }
    
    private String runFunction(BiFunction<String, String, String> o) {
        return o.apply("A", "B");
    }
}
