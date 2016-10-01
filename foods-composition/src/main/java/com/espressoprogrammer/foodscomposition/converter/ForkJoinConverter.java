package com.espressoprogrammer.foodscomposition.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;

public class ForkJoinConverter<T, R> extends RecursiveTask<List<R>> {
    private static final int THRESHOLD = 1_000;

    private final List<T> values;
    private final Function<T, R> map;

    public ForkJoinConverter(List<T> values, Function<T, R> map) {
        this.values = values;
        this.map = map;
    }

    @Override
    protected List<R> compute() {
        if(values.size() <= THRESHOLD) {
            return computeSequentially();
        }

        int halfSize = values.size() / 2;
        ForkJoinConverter<T, R> leftConverter = new ForkJoinConverter<T, R>(values.subList(0, halfSize), map);
        leftConverter.fork();
        ForkJoinConverter<T, R> rightConverter = new ForkJoinConverter<T, R>(values.subList(halfSize, values.size()), map);
        rightConverter.fork();

        List<R> leftResults = leftConverter.join();
        List<R> rightResults = rightConverter.join();
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
