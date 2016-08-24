package com.espressoprogrammer.foodscomposition.test;

import com.espressoprogrammer.foodscomposition.convertor.ForkJoinConvertor;
import com.espressoprogrammer.foodscomposition.dto.Abbrev;
import com.espressoprogrammer.foodscomposition.dto.AbbrevKcal;
import com.espressoprogrammer.foodscomposition.dto.ConvertorKt;
import com.espressoprogrammer.foodscomposition.parser.AbbrevParser;
import com.espressoprogrammer.foodscomposition.parser.StreamAbbrevParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinConvertorTest {

    private static final Logger logger = LoggerFactory.getLogger(ForkJoinConvertorTest.class);

    public static void main(String... args) {
        AbbrevParser abbrevParser = new StreamAbbrevParser();
        Instant start = Instant.now();
        List<Abbrev> abbrevs = abbrevParser.parseFile("/sr28abbr/ABBREV.txt");
        Instant end = Instant.now();
        logger.info ("parsed {} foods: in {} nanoseconds", abbrevs.size(), Duration.between(start, end).getNano());

        start = Instant.now();
        List<AbbrevKcal> abbrevKcals = new ForkJoinPool().invoke(new ForkJoinConvertor<>(abbrevs, ConvertorKt::convert));
        end = Instant.now();
        logger.info ("convert {} foods: in {} nanoseconds with ConvertorKt::convert", abbrevKcals.size(), Duration.between(start, end).getNano());

        start = Instant.now();
        List<AbbrevKcal> abbrevKcalsComplex = new ForkJoinPool().invoke(new ForkJoinConvertor<>(abbrevs, ConvertorKt::complexConvert));
        end = Instant.now();
        logger.info ("convert {} foods: in {} nanoseconds with ConvertorKt::complexConvert", abbrevKcalsComplex.size(), Duration.between(start, end).getNano());
    }


}
