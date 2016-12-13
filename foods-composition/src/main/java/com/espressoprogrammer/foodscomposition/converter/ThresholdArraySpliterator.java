package com.espressoprogrammer.foodscomposition.converter;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ThresholdArraySpliterator<T> implements Spliterator<T> {
    public static final int THRESHOLD = 1_000;

    private final T[] values;
    private int index;
    private final int endIndex;

    public ThresholdArraySpliterator(List<T> values) {
        this((T[]) values.toArray(), 0, values.size());
    }

    private ThresholdArraySpliterator(T[] values, int index, int endIndex) {
        this.values = values;
        this.index = index;
        this.endIndex = endIndex;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> consumer) {
        if(index < endIndex) {
            consumer.accept(values[index]);
            index++;
            return index < endIndex;
        }

        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        if(index < endIndex && size() > THRESHOLD) {

            int middleIndex = index + size() / 2;
            Spliterator<T> spliterator = new ThresholdArraySpliterator<>(values, index, middleIndex);
            index = middleIndex;
            return spliterator;
        }

        return null;
    }

    @Override
    public long estimateSize() {
        return size();
    }

    private int size() {
        return endIndex - index;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL;
    }
}
