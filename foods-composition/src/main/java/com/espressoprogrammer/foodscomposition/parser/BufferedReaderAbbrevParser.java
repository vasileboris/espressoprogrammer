package com.espressoprogrammer.foodscomposition.parser;

import com.espressoprogrammer.foodscomposition.dto.Abbrev;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * It parses ABBREV.txt in the old fashion way with reading lines from a {@link BufferedReader}
 */
public class BufferedReaderAbbrevParser extends CommonAbbrevParser {

    @Override
    public List<Abbrev> parseFile() {
        List<Abbrev> values = new LinkedList<>();
        try (BufferedReader br = Files.newBufferedReader(getAbbrevURI(), StandardCharsets.ISO_8859_1)) {
            String line;
            while((line = br.readLine()) != null) {
                values.add(parseLine(line));
            }
        } catch (Exception ex)  {
            ex.printStackTrace();
        }
        return values;
    }

}
