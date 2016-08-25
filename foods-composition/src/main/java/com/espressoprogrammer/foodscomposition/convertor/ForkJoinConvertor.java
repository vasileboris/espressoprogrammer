package com.espressoprogrammer.foodscomposition.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;

public class ForkJoinConvertor<T, R> extends RecursiveTask<List<R>> {

    private static final int THRESHOLD = 1;

    private final List<T> values;
    private final Function<T, R> map;

    public ForkJoinConvertor(List<T> values, Function<T, R> map) {
        this.values = values;
        this.map = map;
    }

    @Override
    protected List<R> compute() {
        if(values.size() <= THRESHOLD) {
            return computeSequentially();
        }

        int halfSize = values.size() / 2;
        ForkJoinConvertor<T, R> leftConvertor = new ForkJoinConvertor<T, R>(values.subList(0, halfSize), map);
        leftConvertor.fork();
        ForkJoinConvertor<T, R> rightConvertor = new ForkJoinConvertor<T, R>(values.subList(halfSize, values.size()), map);

        List<R> rightResults = rightConvertor.compute();
        List<R> leftResults = leftConvertor.join();
        return mergeResults(rightResults, leftResults);
    }

    private List<R> computeSequentially() {
        List<R> results = new ArrayList<R>(values.size());
        for(T value : values) {
            results.add(map.apply(value));
        }
        return results;
    }

    private List<R> mergeResults(List<R> rightResults, List<R> leftResults) {
        ArrayList<R> results = new ArrayList<>(values.size());
        results.addAll(leftResults);
        results.addAll(rightResults);
        return results;
    }

}
