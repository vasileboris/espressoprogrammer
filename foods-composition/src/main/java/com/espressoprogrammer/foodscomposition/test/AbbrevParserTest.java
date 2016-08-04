package com.espressoprogrammer.foodscomposition.test;

import com.espressoprogrammer.foodscomposition.dto.Abbrev;
import com.espressoprogrammer.foodscomposition.parser.AbbrevParser;
import com.espressoprogrammer.foodscomposition.parser.BufferedReaderAbbrevParser;
import com.espressoprogrammer.foodscomposition.parser.StreamAbbrevParser;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class AbbrevParserTest {

    public static void main(String... args) {
        testAbbrevParser(new BufferedReaderAbbrevParser(), "Old fashion way");
        testAbbrevParser(new StreamAbbrevParser(), "Java 8 streams");
    }

    private static void testAbbrevParser(AbbrevParser abbrevParser, String message) {
        Instant start = Instant.now();
        List<Abbrev> abbrevs = abbrevParser.parseFile();
        System.out.println();
        Instant end = Instant.now();
        System.out.println(message + " parsed " + abbrevs.size() + " foods: in " + Duration.between(start, end).getNano() + " nanoseconds");
    }

}
