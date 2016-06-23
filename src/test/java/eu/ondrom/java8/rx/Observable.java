package eu.ondrom.java8.rx;

import eu.ondrom.java8.rx.Observable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Observable<T> {

    private Stream<T> stream;
    private Iterator<T> iterator;
    private T publishedValue;
    private List<Object[]> observers = new ArrayList<>();

    public static <T> Observable<T> get() {
        final Observable observable = new Observable();
        return observable;
    }

    private Observable() {
        this.stream = Stream.generate(this::supply);
    }
    
    public Stream<T> getStream() {
        return stream;
    }
    
    public <T2> void observe(Stream<T2> observableStream, Consumer<T2> observer) {
        observers.add(new Object[] {observableStream.iterator(), observer});
    }

    private T supply() {
        return publishedValue;
    }
    
    public synchronized void publish(T value) {
        publishedValue = value;
        observers.stream().forEach(observer -> {
            Iterator<?> it = (Iterator)observer[0];
            Consumer<Object> c = (Consumer)observer[1];
            if (it.hasNext()) {
                c.accept(it.next());
            }
        });
    }
    
}
