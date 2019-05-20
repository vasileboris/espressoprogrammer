package com.espressoprogrammer.foodscomposition.converter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

import static com.espressoprogrammer.foodscomposition.converter.ConverterUtil.createValues;
import static org.assertj.core.api.Assertions.assertThat;

public class ThresholdArraySpliteratorTest {

    private List<Integer> results;

    @Before
    public void prepare() {
        results = new ArrayList<>();
    }

    @Test
    public void tryAdvanceWithEmptyList() {
        ThresholdArraySpliterator<Integer> thresholdSpliterator = new ThresholdArraySpliterator<>(new ArrayList<>());

        assertThat(results).isEmpty();
        assertThat(thresholdSpliterator.tryAdvance(results::add)).isFalse();
        assertThat(results).isEmpty();
    }

    @Test
    public void tryAdvanceWithValues() {
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        values.add(3);
        ThresholdArraySpliterator<Integer> thresholdSpliterator = new ThresholdArraySpliterator<>(values);

        assertThat(results).isEmpty();
        assertThat(thresholdSpliterator.tryAdvance(results::add)).isTrue();
        assertThat(thresholdSpliterator.tryAdvance(results::add)).isTrue();
        assertThat(thresholdSpliterator.tryAdvance(results::add)).isFalse();
        assertThat(results)
            .hasSize(3)
            .contains(1, 2, 3);
    }

    @Test
    public void estimateSizeWithEmptyList() {
        ThresholdArraySpliterator<Integer> thresholdSpliterator = new ThresholdArraySpliterator<>(new ArrayList<>());

        assertThat(thresholdSpliterator.estimateSize()).isZero();
    }

    @Test
    public void estimateSizeWithValues() {
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        values.add(3);
        ThresholdArraySpliterator<Integer> thresholdSpliterator = new ThresholdArraySpliterator<>(values);

        assertThat(thresholdSpliterator.estimateSize()).isEqualTo(3);
    }

    @Test
    public void trySplitWithEmptyList() {
        ThresholdArraySpliterator<Integer> thresholdSpliterator = new ThresholdArraySpliterator<>(new ArrayList<>());

        assertThat(thresholdSpliterator.trySplit()).isNull();
    }

    @Test
    public void trySplitWithValuesCountLessThanThreshold() {
        List<Integer> values = createValues(ThresholdArraySpliterator.THRESHOLD - 1);
        ThresholdArraySpliterator<Integer> thresholdSpliterator = new ThresholdArraySpliterator<>(values);

        assertThat(thresholdSpliterator.trySplit()).isNull();
        assertThat(thresholdSpliterator.estimateSize()).isEqualTo(ThresholdArraySpliterator.THRESHOLD - 1);
        List<Integer> secondHalfValues = new ArrayList<>();
        while(thresholdSpliterator.tryAdvance(s -> secondHalfValues.add(s))){}
        for(int i=0; i< secondHalfValues.size(); i++) {
            assertThat(secondHalfValues.get(i)).isEqualTo(values.get(i));
        }
    }

    @Test
    public void tryAdvanceAndThenSplitWithValuesCountLessThanThreshold() {
        List<Integer> values = createValues(ThresholdArraySpliterator.THRESHOLD);
        ThresholdArraySpliterator<Integer> thresholdSpliterator = new ThresholdArraySpliterator<>(values);

        List<Integer> firstHalfValues = new ArrayList<>();
        thresholdSpliterator.tryAdvance(s -> firstHalfValues.add(s));
        for(int i=0; i< firstHalfValues.size(); i++) {
            assertThat(firstHalfValues.get(i)).isEqualTo(values.get(i));
        }

        assertThat(thresholdSpliterator.trySplit()).isNull();
        assertThat(thresholdSpliterator.estimateSize()).isEqualTo(ThresholdArraySpliterator.THRESHOLD - 1);
        List<Integer> secondHalfValues = new ArrayList<>();
        while(thresholdSpliterator.tryAdvance(s -> secondHalfValues.add(s))){}
        for(int i=0; i< secondHalfValues.size(); i++) {
            assertThat(secondHalfValues.get(i)).isEqualTo(values.get(firstHalfValues.size() + i));
        }
    }

    @Test
    public void trySplitWithValuesCountEqualToThreshold() {
        List<Integer> values = createValues(ThresholdArraySpliterator.THRESHOLD);
        ThresholdArraySpliterator<Integer> thresholdSpliterator = new ThresholdArraySpliterator<>(values);

        assertThat(thresholdSpliterator.trySplit()).isNull();
        assertThat(thresholdSpliterator.estimateSize()).isEqualTo(ThresholdArraySpliterator.THRESHOLD);
        List<Integer> secondHalfValues = new ArrayList<>();
        while(thresholdSpliterator.tryAdvance(s -> secondHalfValues.add(s))){}
        for(int i=0; i< secondHalfValues.size(); i++) {
            assertThat(secondHalfValues.get(i)).isEqualTo(values.get(i));
        }
    }

    @Test
    public void tryAdvanceAndThenSplitWithValuesCountEqualToThreshold() {
        List<Integer> values = createValues(ThresholdArraySpliterator.THRESHOLD + 1);
        ThresholdArraySpliterator<Integer> thresholdSpliterator = new ThresholdArraySpliterator<>(values);

        List<Integer> firstHalfValues = new ArrayList<>();
        thresholdSpliterator.tryAdvance(s -> firstHalfValues.add(s));
        for(int i=0; i< firstHalfValues.size(); i++) {
            assertThat(firstHalfValues.get(i)).isEqualTo(values.get(i));
        }

        assertThat(thresholdSpliterator.trySplit()).isNull();
        assertThat(thresholdSpliterator.estimateSize()).isEqualTo(ThresholdArraySpliterator.THRESHOLD);
        List<Integer> secondHalfValues = new ArrayList<>();
        while(thresholdSpliterator.tryAdvance(s -> secondHalfValues.add(s))){}
        for(int i=0; i< secondHalfValues.size(); i++) {
            assertThat(secondHalfValues.get(i)).isEqualTo(values.get(firstHalfValues.size() + i));
        }
    }

    @Test
    public void trySplitWithValuesCountGreaterThanThreshold() {
        List<Integer> values = createValues(ThresholdArraySpliterator.THRESHOLD + 1);

        ThresholdArraySpliterator<Integer> thresholdSpliterator = new ThresholdArraySpliterator<>(values);

        Spliterator<Integer> firstHalfThresholdSpliterator = thresholdSpliterator.trySplit();
        int firstHalfSpliteratorSize = (ThresholdArraySpliterator.THRESHOLD + 1) / 2;

        assertThat(firstHalfThresholdSpliterator.estimateSize()).isEqualTo(firstHalfSpliteratorSize);
        List<Integer> firstHalfValues = new ArrayList<>();
        while(firstHalfThresholdSpliterator.tryAdvance(s -> firstHalfValues.add(s))){}
        for(int i=0; i< firstHalfValues.size(); i++) {
            assertThat(firstHalfValues.get(i)).isEqualTo(values.get(i));
        }

        assertThat(thresholdSpliterator.estimateSize()).isEqualTo(values.size() - firstHalfSpliteratorSize);
        List<Integer> secondHalfValues = new ArrayList<>();
        while(thresholdSpliterator.tryAdvance(s -> secondHalfValues.add(s))){}
        for(int i=0; i< secondHalfValues.size(); i++) {
            assertThat(secondHalfValues.get(i)).isEqualTo(values.get(firstHalfValues.size() + i));
        }
    }

    @Test
    public void tryAdvanceAndThenSplitWithValuesCountGreaterThanThreshold() {
        List<Integer> values = createValues(ThresholdArraySpliterator.THRESHOLD + 2);

        ThresholdArraySpliterator<Integer> thresholdSpliterator = new ThresholdArraySpliterator<>(values);
        List<Integer> firstHalfValues = new ArrayList<>();
        thresholdSpliterator.tryAdvance(s -> firstHalfValues.add(s));

        Spliterator<Integer> firstHalfThresholdSpliterator = thresholdSpliterator.trySplit();
        int firstHalfSpliteratorSize = (ThresholdArraySpliterator.THRESHOLD + 1) / 2;

        assertThat(firstHalfThresholdSpliterator.estimateSize()).isEqualTo(firstHalfSpliteratorSize);
        while(firstHalfThresholdSpliterator.tryAdvance(s -> firstHalfValues.add(s))){}
        for(int i=0; i< firstHalfValues.size(); i++) {
            assertThat(firstHalfValues.get(i)).isEqualTo(values.get(i));
        }

        assertThat(thresholdSpliterator.estimateSize()).isEqualTo(values.size() - firstHalfSpliteratorSize - 1);
        List<Integer> secondHalfValues = new ArrayList<>();
        while(thresholdSpliterator.tryAdvance(s -> secondHalfValues.add(s))){}
        for(int i=0; i< secondHalfValues.size(); i++) {
            assertThat(secondHalfValues.get(i)).isEqualTo(values.get(firstHalfValues.size() + i));
        }
    }
}
