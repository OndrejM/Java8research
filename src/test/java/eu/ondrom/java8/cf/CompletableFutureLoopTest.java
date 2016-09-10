/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ondrom.java8.cf;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author omihalyi
 */
public class CompletableFutureLoopTest {

    @Test
    public void canIterateWithCF_single_thread() throws InterruptedException, ExecutionException, TimeoutException {
        String result = getList()
                .thenCompose(list -> {
                    CompletionStage<String> loopStage = CompletableFuture.completedFuture(""); // initial value is empty string
                    for (String v : list) {
                        loopStage = processItem(v)
                                .thenCombine(loopStage, (processed, loopResult) -> {
                                    return loopResult + processed;
                                });

                    }
                    return loopStage;
                }).toCompletableFuture()
                .get(1, TimeUnit.SECONDS);
        assertEquals("Concatenated string", "1, 2, 3, ", result);
    }

    @Test
    public void canIterateWithCF_multithreaded() throws InterruptedException, ExecutionException, TimeoutException {
        String result = getList()
                .thenCompose(list -> {
                    CompletionStage<String> loopStage = CompletableFuture.completedFuture(""); // initial value is empty string
                    for (String v : list) {
                        loopStage = processItemAsync(v)
                                .thenCombine(loopStage, (processed, loopResult) -> {
                                    return loopResult + processed;
                                });

                    }
                    return loopStage;
                }).toCompletableFuture()
                .get(1, TimeUnit.SECONDS);
        assertEquals("Concatenated string", "1, 2, 3, ", result);
    }

    public CompletionStage<List<String>> getList() {
        return CompletableFuture.completedFuture(Arrays.asList("1", "2", "3"));
    }

    public CompletionStage<String> processItem(String v) {
        v += ", ";
        return CompletableFuture.completedFuture(v);
    }

    public CompletionStage<String> processItemAsync(final String v) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Thread.sleep(Math.abs(new Random().nextLong() % 1000));
                        Logger.getGlobal().info("Thread finished: " + Thread.currentThread().getName());
                        return v + ", ";
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }
}
