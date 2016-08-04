package com.espressoprogrammer.foodscomposition.parser;

import com.espressoprogrammer.foodscomposition.dto.Abbrev;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * It parses ABBREV.txt with Java 8 {@link Stream}
 */
public class StreamAbbrevParser extends CommonAbbrevParser {

    @Override
    public List<Abbrev> parseFile() {
        try (Stream<String> stream = Files.lines(getAbbrevURI(), StandardCharsets.ISO_8859_1)) {
            return stream.map(this::parseLine).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

}
