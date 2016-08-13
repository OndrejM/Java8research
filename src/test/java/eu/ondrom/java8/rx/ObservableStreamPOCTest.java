package eu.ondrom.java8.rx;

import java.util.stream.IntStream;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;
import static org.junit.Assert.*;

@Ignore
public class ObservableStreamPOCTest {
    
    public ObservableStreamPOCTest() {
    }

    private int numberOfObserved;
    
    /* Result - not possible
       Reason: because Streams are pulling, where the activity comes from the receiver side, 
    receiver may block while not knowing whether there are values in the pipeline. 
    Streams API is only blocking, there is no way to listen on new values.
    That leads to this behavior: Do we have something? Yes -> do something with it. No -> wait until something is there.
    */
    @Test
    public void canObserveWithJavaStreams() {
        
        numberOfObserved = 0;
        
        Observable<Integer> ob = Observable.get();
        
        ob.observe(ob.getStream()
//              .limit(10000)
              .filter(i -> i < 50)
                , 
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
