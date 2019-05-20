package com.espressoprogrammer.foodscomposition.test;

import com.espressoprogrammer.foodscomposition.dto.Abbrev;
import com.espressoprogrammer.foodscomposition.parser.AbbrevParser;
import com.espressoprogrammer.foodscomposition.parser.BufferedReaderAbbrevParser;
import com.espressoprogrammer.foodscomposition.parser.StreamAbbrevParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class AbbrevParserTest {
    private static final Logger logger = LoggerFactory.getLogger(AbbrevParserTest.class);

    public static void main(String... args) {
        testAbbrevParser("/sr28abbr/ABBREV.txt", new BufferedReaderAbbrevParser(), "Old fashion way");
        testAbbrevParser("/sr28abbr/ABBREV.txt", new StreamAbbrevParser(), "Java 8 streams");
    }

    private static void testAbbrevParser(String fileName, AbbrevParser abbrevParser, String message) {
        Instant start = Instant.now();
        List<Abbrev> abbrevs = abbrevParser.parseFile(fileName);
        Instant end = Instant.now();
        logger.info ("{} parsed {} foods: in {} nanoseconds", message, abbrevs.size(), Duration.between(start, end).getNano());
    }

}
