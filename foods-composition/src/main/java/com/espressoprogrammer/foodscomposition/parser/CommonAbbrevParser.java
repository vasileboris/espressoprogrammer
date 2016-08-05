package com.espressoprogrammer.foodscomposition.parser;

import com.espressoprogrammer.foodscomposition.dto.Abbrev;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * It contains common functionality needed by implementations of {@link AbbrevParser}
 */
public abstract class CommonAbbrevParser implements AbbrevParser {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected String getString(String rawValue) {
        if(!rawValue.startsWith(TEXT_FIELD_DELIMITER)) {
            return rawValue;
        }

        if(rawValue.length() > 2) {
            return rawValue.substring(1, rawValue.length() -1);
        }

        return "";
    }

    protected Integer getInteger(String rawValue) {
        return Integer.valueOf(rawValue);
    }

    protected Path getAbbrevURI(String fileName) throws URISyntaxException {
        return Paths.get(getClass().getResource(fileName).toURI());
    }

    @Override
    public Abbrev parseLine(String line) {
        String[] lineTokens = line.split(FIELD_SEPARATOR_REGEX);
        return new Abbrev(getString(lineTokens[0]),
            getString(lineTokens[1]),
            getInteger(lineTokens[3]));
    }
}
