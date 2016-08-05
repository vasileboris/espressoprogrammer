package com.espressoprogrammer.foodscomposition.parser;

import com.espressoprogrammer.foodscomposition.dto.Abbrev;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * It parses ABBREV.txt with Java 8 {@link Stream}
 */
public class StreamAbbrevParser extends CommonAbbrevParser {

    @Override
    public List<Abbrev> parseFile(String fileName) {
        logger.info("Parsing {} file", fileName);
        try (Stream<String> stream = Files.lines(Paths.get(getAbbrevURI(fileName)), StandardCharsets.ISO_8859_1)) {
            return stream.map(this::parseLine).collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Failed to parse ABBREV.txt file", ex);
        }
        return new LinkedList<>();
    }

    private URI getAbbrevURI(String fileName) throws URISyntaxException {
        return getClass().getResource(fileName).toURI();
    }

}
