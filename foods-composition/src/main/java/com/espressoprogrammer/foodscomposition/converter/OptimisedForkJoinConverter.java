package com.espressoprogrammer.foodscomposition.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;

public class OptimisedForkJoinConverter<T, R> extends RecursiveTask<List<R>> {
    public static final int THRESHOLD = 1_000;

    private final List<T> values;
    private final Function<T, R> map;

    public OptimisedForkJoinConverter(List<T> values, Function<T, R> map) {
        this.values = values;
        this.map = map;
    }

    @Override
    protected List<R> compute() {
        if(values.size() <= THRESHOLD) {
            return computeSequentially();
        }

        int halfSize = values.size() / 2;
        OptimisedForkJoinConverter<T, R> leftConverter = new OptimisedForkJoinConverter<T, R>(values.subList(0, halfSize), map);
        leftConverter.fork();
        OptimisedForkJoinConverter<T, R> rightConverter = new OptimisedForkJoinConverter<T, R>(values.subList(halfSize, values.size()), map);

        List<R> rightResults = rightConverter.compute();
        List<R> leftResults = leftConverter.join();
        return mergeResults(leftResults, rightResults);
    }

    private List<R> computeSequentially() {
        List<R> results = new ArrayList<R>(values.size());
        for(T value : values) {
            results.add(map.apply(value));
        }
        return results;
    }

    private List<R> mergeResults(List<R> leftResults, List<R> rightResults) {
        ArrayList<R> results = new ArrayList<>(leftResults.size() + rightResults.size());
        results.addAll(leftResults);
        results.addAll(rightResults);
        return results;
    }

}
