package com.espressoprogrammer.competition.primitives.heap;

import org.github.jamm.MemoryMeter;

public class ArraysCompetition {

    public static void main(String... args) {
        MemoryMeter memoryMeter = new MemoryMeter().enableDebug();

        int size = Integer.parseInt(args[0]);
        System.out.println("ByteArray  : " + memoryMeter.measureDeep(new ByteArray(size)));
        System.out.println("ShortArray : " + memoryMeter.measureDeep(new ShortArray(size)));
        System.out.println("IntArray   : " + memoryMeter.measureDeep(new IntArray(size)));
        System.out.println("LongArray  : " + memoryMeter.measureDeep(new LongArray(size)));
    }

}
