package com.espressoprogrammer.foodscomposition.parser;

import com.espressoprogrammer.foodscomposition.dto.Abbrev;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * It parses ABBREV.txt in the old fashion way with reading lines from a {@link BufferedReader}
 */
public class BufferedReaderAbbrevParser extends CommonAbbrevParser {

    @Override
    public List<Abbrev> parseFile(String fileName) {
        logger.info("Parsing {} file", fileName);
        List<Abbrev> values = new LinkedList<>();
        try (BufferedReader br =  new BufferedReader(new InputStreamReader(getAbbrevInputStream(fileName), StandardCharsets.ISO_8859_1))) {
            String line;
            while((line = br.readLine()) != null) {
                values.add(parseLine(line));
            }
        } catch (Exception ex)  {
            logger.error("Failed to parse ABBREV.txt file", ex);
        }
        return values;
    }

    private InputStream getAbbrevInputStream(String fileName) {
        return getClass().getResourceAsStream(fileName);
    }

}
