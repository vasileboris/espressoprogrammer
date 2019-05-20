package com.espressoprogrammer.foodscomposition.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;

public class ForkJoinSpliteratorConverter<T, R> extends RecursiveTask<List<R>> {
    public static final int THRESHOLD = 1_000;

    private final Spliterator<T> spliterator;
    private final Function<T, R> map;

    public ForkJoinSpliteratorConverter(List<T> values, Function<T, R> map) {
        this.spliterator = values.spliterator();
        this.map = map;
    }

    private ForkJoinSpliteratorConverter(Spliterator<T> spliterator, Function<T, R> map) {
        this.spliterator = spliterator;
        this.map = map;
    }

    @Override
    protected List<R> compute() {
        if(spliterator == null) {
            return new ArrayList<>();
        }

        if(spliterator.estimateSize() <= THRESHOLD) {
            return computeSequentially();
        }

        ForkJoinSpliteratorConverter<T, R> leftConverter = new ForkJoinSpliteratorConverter<T, R>(spliterator.trySplit(), map);
        leftConverter.fork();
        ForkJoinSpliteratorConverter<T, R> rightConverter = new ForkJoinSpliteratorConverter<T, R>(spliterator, map);
        rightConverter.fork();

        List<R> leftResults = leftConverter.join();
        List<R> rightResults = rightConverter.join();
        return mergeResults(leftResults, rightResults);
    }

    private List<R> computeSequentially() {
        List<R> results = new ArrayList<>((int) spliterator.estimateSize());
        spliterator.forEachRemaining(t -> results.add(map.apply(t)));
        return results;
    }

    private List<R> mergeResults(List<R> leftResults, List<R> rightResults) {
        ArrayList<R> results = new ArrayList<>(leftResults.size() + rightResults.size());
        results.addAll(leftResults);
        results.addAll(rightResults);
        return results;
    }

}
