package com.zzq.graduationproject;

import java.time.Clock;
import org.junit.Test;

public class SimpleTest {

    @Test
    public void test() {
        SimpleTest simpleTest = new SimpleTest();
        print(simpleTest::printNumber);
    }

    private void print(VoidFunction<Integer> function) {
        long start = Clock.systemUTC().millis();
        for (int i = 0; i < 10000 ; i++) {
            function.apply(i);
        }
        long end = Clock.systemUTC().millis();
        System.out.println(end - start);
    }

    private void printNumber(int i) {
        System.out.println(i);
    }
}

@FunctionalInterface
interface VoidFunction<T> {
    void apply(T t);
}
