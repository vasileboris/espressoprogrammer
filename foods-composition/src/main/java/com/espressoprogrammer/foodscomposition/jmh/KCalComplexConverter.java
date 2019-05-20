package com.espressoprogrammer.foodscomposition.jmh;

import com.espressoprogrammer.foodscomposition.converter.*;
import com.espressoprogrammer.foodscomposition.dto.Abbrev;
import com.espressoprogrammer.foodscomposition.dto.AbbrevKcal;
import com.espressoprogrammer.foodscomposition.dto.ConverterKt;
import com.espressoprogrammer.foodscomposition.parser.BufferedReaderAbbrevParser;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 25)
@Measurement(iterations = 100)
@Fork(1)
@State(Scope.Thread)
public class KCalComplexConverter {
    private static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

    private List<Abbrev> abbrevs;

    @Setup(value = Level.Trial)
    public void setup() {
        abbrevs = new BufferedReaderAbbrevParser().parseFile("/sr28abbr/ABBREV.txt");
    }

    @Benchmark
    public void baseline() {
    }

    @Benchmark
    public void forEach(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = new ArrayList<>(abbrevs.size());
        for(Abbrev abbrev : abbrevs) {
            abbrevKcals.add(ConverterKt.complexConvert(abbrev));
        }
        blackhole.consume(abbrevKcals);
    }

    @Benchmark
    public void sequentialStream(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = abbrevs.stream()
            .map(ConverterKt::complexConvert)
            .collect(Collectors.toList());
        blackhole.consume(abbrevKcals);
    }

    @Benchmark
    public void parallelStream(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = abbrevs.parallelStream()
            .map(ConverterKt::complexConvert)
            .collect(Collectors.toList());
        blackhole.consume(abbrevKcals);
    }

    @Benchmark
    public void forkJoin(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = FORK_JOIN_POOL
            .invoke(new ForkJoinConverter<>(abbrevs, ConverterKt::complexConvert));
        blackhole.consume(abbrevKcals);
    }

    @Benchmark
    public void optimizedForkJoin(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = FORK_JOIN_POOL
            .invoke(new OptimisedForkJoinConverter<>(abbrevs, ConverterKt::complexConvert));
        blackhole.consume(abbrevKcals);
    }

    @Benchmark
    public void forkJoinSpliterator(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = FORK_JOIN_POOL
            .invoke(new ForkJoinSpliteratorConverter<>(abbrevs, ConverterKt::complexConvert));
        blackhole.consume(abbrevKcals);
    }

    @Benchmark
    public void forkJoinThresholdSpliterator(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = FORK_JOIN_POOL
            .invoke(new ForkJoinThresholdSpliteratorConverter<>(abbrevs, ConverterKt::complexConvert));
        blackhole.consume(abbrevKcals);
    }

    @Benchmark
    public void parallelStreamThresholdSpliterator(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = StreamSupport.stream(new ThresholdSpliterator<>(abbrevs), true)
            .map(ConverterKt::complexConvert)
            .collect(Collectors.toList());
        blackhole.consume(abbrevKcals);
    }

    @Benchmark
    public void forkJoinThresholdArraySpliterator(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = FORK_JOIN_POOL
                .invoke(new ForkJoinThresholdArraySpliteratorConverter<>(abbrevs, ConverterKt::complexConvert));
        blackhole.consume(abbrevKcals);
    }

    @Benchmark
    public void parallelStreamThresholdArraySpliterator(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = StreamSupport.stream(new ThresholdArraySpliterator<>(abbrevs), true)
                .map(ConverterKt::complexConvert)
                .collect(Collectors.toList());
        blackhole.consume(abbrevKcals);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(KCalComplexConverter.class.getSimpleName())
            .build();

        new Runner(opt).run();
    }

}
