package eu.ondrom.java8.rx;

import java.util.stream.IntStream;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObservableStreamPOCTest {
    
    public ObservableStreamPOCTest() {
    }

    private int numberOfObserved;
    
    @Test
    public void canObserveWithJavaStreams() {
        
        numberOfObserved = 0;
        
        Observable<Integer> ob = Observable.get();
        
        ob.observe(ob.getStream()
              .limit(100)
              .filter(i -> i % 2 == 0), 
            this::observeEvent);
        
        publishEvents(ob, 100);
        
        assertThat("number of observed events", 
                numberOfObserved, is(equalTo(50)));
    }

    private void observeEvent(Integer x) {
        System.out.println("Observed: " + x);
        numberOfObserved++;
    }

    private void publishEvents(Observable<Integer> ob, int count) {
        IntStream.range(0, count).forEach(i -> {
            ob.publish(i);
        });
    }

}
