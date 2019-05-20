package com.espressoprogrammer.foodscomposition.converter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.espressoprogrammer.foodscomposition.converter.ConverterUtil.FORK_JOIN_POOL;
import static com.espressoprogrammer.foodscomposition.converter.ConverterUtil.converter;
import static com.espressoprogrammer.foodscomposition.converter.ConverterUtil.createExpectedResults;
import static com.espressoprogrammer.foodscomposition.converter.ConverterUtil.createValues;
import static com.espressoprogrammer.foodscomposition.converter.ThresholdSpliterator.THRESHOLD;
import static org.assertj.core.api.Assertions.assertThat;

public class ForkJoinThresholdArraySpliteratorConverterTest {

    @Test
    public void computeWithEmptyList() {
        assertThat(FORK_JOIN_POOL.invoke(new ForkJoinThresholdArraySpliteratorConverter<>(new ArrayList<>(), converter))).isEmpty();
    }

    @Test
    public void computeWithValuesCountLessThanThreshold() {
        List<Integer> values = createValues(THRESHOLD - 1);
        List<String> expectedResults = createExpectedResults(THRESHOLD - 1);
        assertThat(FORK_JOIN_POOL.invoke(new ForkJoinThresholdArraySpliteratorConverter<>(values, converter)))
            .isEqualTo(expectedResults);
    }

    @Test
    public void computeWithValuesCountEqualToThreshold() {
        List<Integer> values = createValues(THRESHOLD);
        List<String> expectedResults = createExpectedResults(THRESHOLD);
        assertThat(FORK_JOIN_POOL.invoke(new ForkJoinThresholdArraySpliteratorConverter<>(values, converter)))
            .isEqualTo(expectedResults);
    }

    @Test
    public void computeWithValuesCountGreaterThanThreshold() {
        List<Integer> values = createValues(THRESHOLD + 1);
        List<String> expectedResults = createExpectedResults(THRESHOLD + 1);
        assertThat(FORK_JOIN_POOL.invoke(new ForkJoinThresholdArraySpliteratorConverter<>(values, converter)))
            .isEqualTo(expectedResults);
    }
}
