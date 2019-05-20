package com.espressoprogrammer.foodscomposition.parser;

import com.espressoprogrammer.foodscomposition.dto.Abbrev;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * It contains common functionality needed by implementations of {@link AbbrevParser}
 */
public abstract class CommonAbbrevParser implements AbbrevParser {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Abbrev parseLine(String line) {
        String[] lineTokens = line.split(FIELD_SEPARATOR_REGEX);
        Abbrev abbrev = new Abbrev(getString(lineTokens[0]),
            getString(lineTokens[1]),
            getInteger(lineTokens[3]));
        abbrev.setGmWt1(getDouble(lineTokens[48]));
        abbrev.setGmWtDesc1(getString(lineTokens[49]));
        abbrev.setGmWt2(getDouble(lineTokens[50]));
        abbrev.setGmWtDesc2(getString(lineTokens[51]));
        return abbrev;
    }

    private String getString(String rawValue) {
        if(!rawValue.startsWith(TEXT_FIELD_DELIMITER)) {
            return rawValue;
        }

        if(rawValue.length() > 2) {
            return rawValue.substring(1, rawValue.length() -1);
        }

        return "";
    }

    private Integer getInteger(String rawValue) {
        try {
            return Integer.valueOf(rawValue);
        } catch(NumberFormatException nfEx) {
            return 0;
        }
    }

    private Double getDouble(String rawValue) {
        try {
        return Double.valueOf(rawValue);
        } catch(NumberFormatException nfEx) {
            return 0.0;
        }
    }
}
