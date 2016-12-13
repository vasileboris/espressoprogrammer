package com.espressoprogrammer.foodscomposition.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;

public class ForkJoinThresholdArraySpliteratorConverter<T, R> extends RecursiveTask<List<R>> {
    private final Spliterator<T> spliterator;
    private final Function<T, R> map;

    public ForkJoinThresholdArraySpliteratorConverter(List<T> values, Function<T, R> map) {
        this.spliterator = new ThresholdArraySpliterator<>(values);
        this.map = map;
    }

    private ForkJoinThresholdArraySpliteratorConverter(Spliterator<T> spliterator, Function<T, R> map) {
        this.spliterator = spliterator;
        this.map = map;
    }

    @Override
    protected List<R> compute() {
        if(spliterator == null) {
            return new ArrayList<>();
        }

        Spliterator<T> firstHalfSpliterator = spliterator.trySplit();
        if(firstHalfSpliterator == null) {
            return computeSequentially();
        }

        ForkJoinThresholdArraySpliteratorConverter<T, R> leftConverter = new ForkJoinThresholdArraySpliteratorConverter<>(firstHalfSpliterator, map);
        leftConverter.fork();
        ForkJoinThresholdArraySpliteratorConverter<T, R> rightConverter = new ForkJoinThresholdArraySpliteratorConverter<>(spliterator, map);
        rightConverter.fork();

        List<R> leftResults = leftConverter.join();
        List<R> rightResults = rightConverter.join();
        return mergeResults(leftResults, rightResults);
    }

    private List<R> computeSequentially() {
        List<R> results = new ArrayList<>((int) spliterator.estimateSize());
        while (spliterator.tryAdvance(t -> results.add(map.apply(t)))){}
        return results;
    }

    private List<R> mergeResults(List<R> leftResults, List<R> rightResults) {
        ArrayList<R> results = new ArrayList<>(leftResults.size() + rightResults.size());
        results.addAll(leftResults);
        results.addAll(rightResults);
        return results;
    }

}
