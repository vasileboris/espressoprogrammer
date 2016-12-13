package com.espressoprogrammer.foodscomposition.converter;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConverterUtil {

    public static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

    public static final Function<Integer, String> converter = i -> i.toString();

    public static List<Integer> createValues(int size) {
        return IntStream
            .range(0, size)
            .boxed()
            .collect(Collectors.toList());
    }

    public static List<String> createExpectedResults(int size) {
        return IntStream
            .range(0, size)
            .boxed()
            .map(i -> i.toString())
            .collect(Collectors.toList());
    }
}
