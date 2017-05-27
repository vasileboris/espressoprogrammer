package com.espressoprogrammer.reduce;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReduceTest {

    public static void main(String... args) {
        List<Integer> values = Arrays.asList(new Integer[]{1, 2, 3, 4, 5});
        Optional<Integer> sum1 = values.stream().reduce((previous, current) -> {
            System.out.println("sum1 - reduce(previous: " + previous + ", current: " + current + ")");
            return previous + current;
        });
        System.out.println("sum1: " + sum1);

        Integer sum2 = values.stream().reduce(0, (previous, current) -> {
            System.out.println("sum2 - reduce(previous: " + previous + ", current: " + current + ")");
            return previous + current;
        });
        System.out.println("sum2: " + sum2);
    }

}
