package com.espressoprogrammer.competition.primitives.heap;

import org.github.jamm.MemoryMeter;

public class Competition {

    public static void main(String... args) {
        MemoryMeter memoryMeter = new MemoryMeter().enableDebug();

        System.out.println("ByteFields: " + memoryMeter.measureDeep(new ByteFields()));
        System.out.println("ByteArray : " + memoryMeter.measureDeep(new ByteArray(Integer.parseInt(args[0]))));

        System.out.println("------------------");

        System.out.println("ShortFields: " + memoryMeter.measureDeep(new ShortFields()));
        System.out.println("ShortArray : " + memoryMeter.measureDeep(new ShortArray(Integer.parseInt(args[1]))));

        System.out.println("------------------");

        System.out.println("IntFields: " + memoryMeter.measureDeep(new IntFields()));
        System.out.println("IntArray : " + memoryMeter.measureDeep(new IntArray(Integer.parseInt(args[2]))));

        System.out.println("------------------");

        System.out.println("LongFields: " + memoryMeter.measureDeep(new LongFields()));
        System.out.println("LongArray : " + memoryMeter.measureDeep(new LongArray(Integer.parseInt(args[3]))));
    }

}
