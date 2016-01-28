package com.espressoprogrammer.competition.primitives.heap;

import org.github.jamm.MemoryMeter;

public class FieldsCompetition {

    public static void main(String... args) {
        MemoryMeter memoryMeter = new MemoryMeter().enableDebug();

        System.out.println("No fields : " + memoryMeter.measureDeep(new ComposeFields()));

        System.out.println("ByteField : " + memoryMeter.measureDeep(new ComposeFields()
                .setByteField(new ByteField())));

        System.out.println("ShortField: " + memoryMeter.measureDeep(new ComposeFields()
                .setShortField(new ShortField())));

        System.out.println("IntField  : " + memoryMeter.measureDeep(new ComposeFields()
                .setIntField(new IntField())));

        System.out.println("LongField : " + memoryMeter.measureDeep(new ComposeFields()
                .setLongField(new LongField())));

        System.out.println("All fields: " + memoryMeter.measureDeep(new ComposeFields()
                .setByteField(new ByteField())
                .setShortField(new ShortField())
                .setIntField(new IntField())
                .setLongField(new LongField())));
    }

}
