package com.espressoprogrammer.foodscomposition.parser;

import com.espressoprogrammer.foodscomposition.dto.Abbrev;

import java.util.List;

/**
 * It contains methods to parse ABBREV.txt file into {@link Abbrev} instances
 */
public interface AbbrevParser {

    String FIELD_SEPARATOR = "^";
    String FIELD_SEPARATOR_REGEX = "\\" + FIELD_SEPARATOR;
    String TEXT_FIELD_DELIMITER = "~";

    Abbrev parseLine(String line);

    List<Abbrev> parseFile(String fileName);

}
