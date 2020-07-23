package com.zzq.test;

import java.util.stream.IntStream;
import org.junit.Test;

/**
 * @author Sherlock
 * @copyright freeman
 * @since 2020/6/28 11:09
 */
public class MainTest {

    @Test
    public void test01() {
        int[] ints = new int[10000];
        for (int i = 0; i < 10000 ; i++) {
            ints[i] = i;
        }
        long start = System.currentTimeMillis();
        long sum = IntStream.of(ints).sum();
        System.out.println(sum);
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void test02() {
        int[] ints = new int[10000];
        for (int i = 0; i < 10000 ; i++) {
            ints[i] = i;
        }
        long start = System.currentTimeMillis();
        long sum = IntStream.of(ints).parallel().sum();
        System.out.println(sum);
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void test03() {
        int[] ints = new int[10000];
        for (int i = 0; i < 10000 ; i++) {
            ints[i] = i;
        }
        long start = System.currentTimeMillis();
        long sum = 0;
        for (int i = 0; i < 10000; i++) {
            sum+=i;
        }
        System.out.println(sum);
        System.out.println(System.currentTimeMillis() - start);
    }

}
