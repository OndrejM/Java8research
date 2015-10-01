/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ondrom.java;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ondro
 */
public class GenericsTest {
    
     @Test
     public void hello() {
         String s = getCopy("X");
     }
     
     private <T> T getCopy(T x) {
         return x;
     }
}
