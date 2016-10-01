package com.espressoprogrammer.foodscomposition.test;

import com.espressoprogrammer.foodscomposition.converter.ForkJoinConverter;
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

public class ForkJoinConverterTest {
    private static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();
    private static final Logger logger = LoggerFactory.getLogger(ForkJoinConverterTest.class);

    public static void main(String... args) {
        AbbrevParser abbrevParser = new StreamAbbrevParser();
        Instant start = Instant.now();
        List<Abbrev> abbrevs = abbrevParser.parseFile("/sr28abbr/ABBREV.txt");
        Instant end = Instant.now();
        logger.info ("parsed {} foods: in {} nanoseconds", abbrevs.size(), Duration.between(start, end).getNano());

        start = Instant.now();
        List<AbbrevKcal> abbrevKcals = FORK_JOIN_POOL.invoke(new ForkJoinConverter<>(abbrevs, ConverterKt::convert));
        end = Instant.now();
        logger.info ("convert {} foods: in {} nanoseconds with ConverterKt::convert", abbrevKcals.size(), Duration.between(start, end).getNano());

        start = Instant.now();
        List<AbbrevKcal> abbrevKcalsComplex = FORK_JOIN_POOL.invoke(new ForkJoinConverter<>(abbrevs, ConverterKt::complexConvert));
        end = Instant.now();
        logger.info ("convert {} foods: in {} nanoseconds with ConverterKt::complexConvert", abbrevKcalsComplex.size(), Duration.between(start, end).getNano());
    }


}
