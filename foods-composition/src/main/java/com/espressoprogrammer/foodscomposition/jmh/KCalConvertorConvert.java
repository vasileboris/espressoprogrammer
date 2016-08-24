package com.espressoprogrammer.foodscomposition.jmh;

import com.espressoprogrammer.foodscomposition.convertor.ForkJoinConvertor;
import com.espressoprogrammer.foodscomposition.dto.Abbrev;
import com.espressoprogrammer.foodscomposition.dto.AbbrevKcal;
import com.espressoprogrammer.foodscomposition.dto.ConvertorKt;
import com.espressoprogrammer.foodscomposition.parser.StreamAbbrevParser;
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

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class KCalConvertorConvert {

    List<Abbrev> abbrevs = new StreamAbbrevParser().parseFile("/sr28abbr/ABBREV.txt");

    @Benchmark
    public void baseline() {
    }

    @Benchmark
    public void forEach(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = new ArrayList<>(abbrevs.size());
        for(Abbrev abbrev : abbrevs) {
            abbrevKcals.add(ConvertorKt.convert(abbrev));
        }
        blackhole.consume(abbrevKcals);
    }

    @Benchmark
    public void sequentialStream(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = abbrevs.stream()
            .map(ConvertorKt::convert)
            .collect(Collectors.toList());
        blackhole.consume(abbrevKcals);
    }

    @Benchmark
    public void parallelStream(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = abbrevs.parallelStream()
            .map(ConvertorKt::convert)
            .collect(Collectors.toList());
        blackhole.consume(abbrevKcals);
    }

    //@Benchmark
    public void forkJoinStream(Blackhole blackhole) {
        List<AbbrevKcal> abbrevKcals = new ForkJoinPool()
            .invoke(new ForkJoinConvertor<>(abbrevs, ConvertorKt::convert));
        blackhole.consume(abbrevKcals);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(KCalConvertorConvert.class.getSimpleName())
            .warmupIterations(25)
            .measurementIterations(100)
            .forks(1)
            .build();

        new Runner(opt).run();
    }

}
