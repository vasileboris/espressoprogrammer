package com.espressoprogrammer.samples.parser.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class LenientDateParser {

    public static void main(String... args) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        dateFormat.setLenient(true);
        System.out.println(dateFormat.parse("2016-05-09"));
        System.out.println(dateFormat.parse("2016-17-09"));
        System.out.println(dateFormat.parse("2016-05-32"));

        dateFormat.setLenient(false);
        System.out.println(dateFormat.parse("2016-05-09"));
        System.out.println(dateFormat.parse("2016-17-09"));
        System.out.println(dateFormat.parse("2016-05-32"));
    }

}
