package com.espressoprogrammer.foodscomposition.test;

import com.espressoprogrammer.foodscomposition.converter.ForkJoinThresholdSpliteratorConverter;
import com.espressoprogrammer.foodscomposition.dto.Abbrev;
import com.espressoprogrammer.foodscomposition.dto.AbbrevKcal;
import com.espressoprogrammer.foodscomposition.dto.ConverterKt;
import com.espressoprogrammer.foodscomposition.parser.AbbrevParser;
import com.espressoprogrammer.foodscomposition.parser.StreamAbbrevParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class forkJoinThresholdSpliteratorConverterTest {
    private static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();
    private static final Logger logger = LoggerFactory.getLogger(forkJoinThresholdSpliteratorConverterTest.class);

    public static void main(String... args) {
        AbbrevParser abbrevParser = new StreamAbbrevParser();
        Instant start = Instant.now();
        List<Abbrev> abbrevs = abbrevParser.parseFile("/sr28abbr/ABBREV.txt");
        Instant end = Instant.now();
        logger.info ("parsed {} foods: in {} nanoseconds", abbrevs.size(), Duration.between(start, end).getNano());

        start = Instant.now();
        List<AbbrevKcal> abbrevKcals = FORK_JOIN_POOL.invoke(new ForkJoinThresholdSpliteratorConverter<>(abbrevs, ConverterKt::convert));
        end = Instant.now();
        logger.info ("convert {} foods: in {} nanoseconds with ForkJoinSpliteratorConverter and ConverterKt::convert", abbrevKcals.size(), Duration.between(start, end).getNano());

        start = Instant.now();
        List<AbbrevKcal> abbrevKcalsComplex = FORK_JOIN_POOL.invoke(new ForkJoinThresholdSpliteratorConverter<>(abbrevs, ConverterKt::complexConvert));
        end = Instant.now();
        logger.info ("convert {} foods: in {} nanoseconds with ForkJoinSpliteratorConverter and ConverterKt::complexConvert", abbrevKcalsComplex.size(), Duration.between(start, end).getNano());
    }


}
