package com.espressoprogrammer.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class IntegralPrimitivesOperations {

    byte bOp1 = 64;
    byte bOp2 = 21;

    short sOp1 = 64;
    short sOp2 = 21;

    int iOp1 = 64;
    int iOp2 = 21;

    char cOp1 = 64;
    char cOp2 = 21;

    long lOp1 = 64;
    long lOp2 = 21;

    @Benchmark
    public void baseline() {
    }

    /*
     * Addition
     */

    @Benchmark
    public byte measureAddByte() {
        return (byte) (bOp1 + bOp2);
    }

    @Benchmark
    public short measureAddShort() {
        return (short) (sOp1 + sOp2);
    }

    @Benchmark
    public int measureAddInt() {
        return iOp1 + iOp2;
    }

    @Benchmark
    public char measureAddChar() {
        return (char) (cOp1 + cOp2);
    }

    @Benchmark
    public long measureAddLong() {
        return lOp1 + lOp2;
    }

    /*
     * Subtraction
     */

    @Benchmark
    public byte measureSubByte() {
        return (byte) (bOp1 - bOp2);
    }

    @Benchmark
    public short measureSubShort() {
        return (short) (sOp1 - sOp2);
    }

    @Benchmark
    public int measureSubInt() {
        return iOp1 - iOp2;
    }

    @Benchmark
    public char measureSubChar() {
        return (char) (cOp1 - cOp2);
    }

    @Benchmark
    public long measureSubLong() {
        return lOp1 - lOp2;
    }

    /*
     * Multiplication
     */

    @Benchmark
    public byte measureMultByte() {
        return (byte) (bOp1 * bOp2);
    }

    @Benchmark
    public short measureMultShort() {
        return (short) (sOp1 * sOp2);
    }

    @Benchmark
    public int measureMultInt() {
        return iOp1 * iOp2;
    }

    @Benchmark
    public char measureMultChar() {
        return (char) (cOp1 * cOp2);
    }

    @Benchmark
    public long measureMultLong() {
        return lOp1 * lOp2;
    }

    /*
     * Division
     */

    @Benchmark
    public byte measureDivByte() {
        return (byte) (bOp1 / bOp2);
    }

    @Benchmark
    public short measureDivShort() {
        return (short) (sOp1 / sOp2);
    }

    @Benchmark
    public int measureDivInt() {
        return iOp1 / iOp2;
    }

    @Benchmark
    public char measureDivChar() {
        return (char) (cOp1 / cOp2);
    }

    @Benchmark
    public long measureDivLong() {
        return lOp1 / lOp2;
    }

    /*
     * Modulo
     */

    @Benchmark
    public byte measureModByte() {
        return (byte) (bOp1 % bOp2);
    }

    @Benchmark
    public short measureModShort() {
        return (short) (sOp1 % sOp2);
    }

    @Benchmark
    public int measureModInt() {
        return iOp1 % iOp2;
    }

    @Benchmark
    public char measureModChar() {
        return (char) (cOp1 % cOp2);
    }

    @Benchmark
    public long measureModLong() {
        return lOp1 % lOp2;
    }

    /*
     * Increment
     */

    @Benchmark
    public byte measureIncByte() {
        return ++bOp1;
    }

    @Benchmark
    public short measureIncShort() {
        return ++sOp1;
    }

    @Benchmark
    public int measureIncInt() {
        return ++iOp1;
    }

    @Benchmark
    public char measureIncChar() {
        return ++cOp1 ;
    }

    @Benchmark
    public long measureIncLong() {
        return ++lOp1;
    }

    /*
     * Decremenet
     */

    @Benchmark
    public byte measureDecByte() {
        return --bOp1;
    }

    @Benchmark
    public short measureDecShort() {
        return --sOp1;
    }

    @Benchmark
    public int measureDecInt() {
        return --iOp1;
    }

    @Benchmark
    public char measureDecChar() {
        return --cOp1 ;
    }

    @Benchmark
    public long measureDecLong() {
        return --lOp1;
    }

    /*
     * All
     */

    @Benchmark
    public byte measureAllByte() {
        return (byte) (++bOp1 * bOp2 + bOp1 / --bOp2 - --bOp1 % bOp2 + bOp2 * ++bOp1 - ++bOp2 / bOp1 + bOp2 % --bOp1);
    }

    @Benchmark
    public short measureAllShort() {
        return (short) (++sOp1 * sOp2 + sOp1 / --sOp2 - --sOp1 % sOp2 + sOp2 * ++sOp1 - ++sOp2 / sOp1 + sOp2 % --sOp1);
    }

    @Benchmark
    public int measureAllInt() {
        return ++iOp1 * iOp2 + iOp1 / --iOp2 - --iOp1 % iOp2 + iOp2 * ++iOp1 - ++iOp2 / iOp1 + iOp2 % --iOp1;
    }

    @Benchmark
    public char measureAllChar() {
        return (char) (++cOp1 * cOp2 + cOp1 / --cOp2 - --cOp1 % cOp2 + cOp2 * ++cOp1 - ++cOp2 * cOp1 + cOp2 % --cOp1);
    }

    @Benchmark
    public long measureAllLong() {
        return ++lOp1 * lOp2 + lOp1 / --lOp2 - --lOp1 % lOp2 + lOp2 * ++lOp1 - ++lOp2 / lOp1 + lOp2 % --lOp1;
    }

}
