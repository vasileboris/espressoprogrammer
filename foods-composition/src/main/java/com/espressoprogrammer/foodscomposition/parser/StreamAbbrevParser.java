package com.espressoprogrammer.foodscomposition.parser;

import com.espressoprogrammer.foodscomposition.dto.Abbrev;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * It parses ABBREV.txt with Java 8 {@link Stream}
 */
public class StreamAbbrevParser extends CommonAbbrevParser {

    private static final String JAR_SCHEME = "jar";

    @Override
    public List<Abbrev> parseFile(String fileName) {
        logger.info("Parsing {} file", fileName);
        URI abbrevURI = getAbbrevURI(fileName);
        if(JAR_SCHEME.equals(abbrevURI.getScheme())) {
            return parseJarFile(abbrevURI);
        }
        return parseFileSystemFile(abbrevURI);
    }

    private List<Abbrev> parseJarFile(URI abbrevURI) {
        try (FileSystem jarFileSystem = FileSystems.newFileSystem(createJarURI(abbrevURI), Collections.EMPTY_MAP);
             Stream<String> stream = Files.lines(getAbbrevPath(abbrevURI), StandardCharsets.ISO_8859_1)) {
            return parseStream(stream);
        } catch (Exception ex) {
            logger.error("Failed to parse ABBREV.txt file", ex);
        }
        return new ArrayList<>();
    }

    private URI createJarURI(URI abbrevURI) {
        return URI.create(JAR_SCHEME + ":" + abbrevURI.getSchemeSpecificPart().split("!")[0]);
    }

    private List<Abbrev> parseFileSystemFile(URI abbrevURI) {
        try (Stream<String> stream = Files.lines(getAbbrevPath(abbrevURI), StandardCharsets.ISO_8859_1)) {
            return parseStream(stream);
        } catch (Exception ex) {
            logger.error("Failed to parse ABBREV.txt file", ex);
        }
        return new ArrayList<>();
    }

    private List<Abbrev> parseStream(Stream<String> stream) {
        return stream.map(this::parseLine).collect(Collectors.toList());
    }

    private Path getAbbrevPath(URI abbrevURI) {
        return Paths.get(abbrevURI);
    }

    private URI getAbbrevURI(String fileName) {
        try {
            return getClass().getResource(fileName).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
