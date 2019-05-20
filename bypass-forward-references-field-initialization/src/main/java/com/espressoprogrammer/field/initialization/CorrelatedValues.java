package com.espressoprogrammer.field.initialization;

public class CorrelatedValues {

    private int value1 = 1;
    private int value2 = value1 + 1;

    public static void main(String... args) {
        System.out.println(new CorrelatedValues().value1);
        System.out.println(new CorrelatedValues().value2);
    }

}
