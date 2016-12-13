package com.espressoprogrammer.foodscomposition.converter;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ThresholdSpliterator<T> implements Spliterator<T> {
    public static final int THRESHOLD = 1_000;

    private final List<T> values;
    private int index;
    private final int endIndex;

    public ThresholdSpliterator(List<T> values) {
        this(values, 0, values.size());
    }

    private ThresholdSpliterator(List<T> values, int index, int endIndex) {
        this.values = values;
        this.index = index;
        this.endIndex = endIndex;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> consumer) {
        if(index < endIndex) {
            consumer.accept(values.get(index));
            index++;
            return index < endIndex;
        }

        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        if(index < endIndex && size() > THRESHOLD) {

            int middleIndex = index + size() / 2;
            Spliterator<T> spliterator = new ThresholdSpliterator<>(values, index, middleIndex);
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
