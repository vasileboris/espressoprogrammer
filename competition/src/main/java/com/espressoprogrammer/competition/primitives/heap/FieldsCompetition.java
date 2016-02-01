package com.espressoprogrammer.competition.primitives.heap;

import org.github.jamm.MemoryMeter;

public class FieldsCompetition {

    public static void main(String... args) {
        MemoryMeter memoryMeter = new MemoryMeter().enableDebug();

        System.out.println("ByteField : " + memoryMeter.measureDeep(new ByteField()));
        System.out.println("ShortField: " + memoryMeter.measureDeep(new ShortField()));
        System.out.println("IntField  : " + memoryMeter.measureDeep(new IntField()));
        System.out.println("LongField : " + memoryMeter.measureDeep(new LongField()));
    }

}
