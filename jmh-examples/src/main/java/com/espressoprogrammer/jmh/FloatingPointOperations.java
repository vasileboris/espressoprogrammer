package com.espressoprogrammer.jmh;

import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class FloatingPointOperations {

    private int scale = 2;

    float fOp1 = 123.45f;
    float fOp2 = 543.21f;

    double dOp1 = 123.45d;
    double dOp2 = 543.21d;

    BigDecimal bdOp1 = new BigDecimal("123.45");
    BigDecimal bdOp2 = new BigDecimal("543.21");

    @Benchmark
    public void baseline() {
    }

    /*
     * Addition
     */

    @Benchmark
    public float measureAddFloat() {
        return fOp1 + fOp2;
    }

    @Benchmark
    public double measureAddDouble() {
        return dOp1 + dOp2;
    }

    @Benchmark
    public BigDecimal measureAddBigDecimal() {
        return bdOp1.add(bdOp2);
    }

    /*
     * Subtraction
     */

    @Benchmark
    public float measureSubFloat() {
        return fOp1 - fOp2;
    }

    @Benchmark
    public double measureSubDouble() {
        return dOp1 - dOp2;
    }

    @Benchmark
    public BigDecimal measureSubBigDecimal() {
        return bdOp1.subtract(bdOp2);
    }

    /*
     * Multiplication
     */

    @Benchmark
    public float measureMultFloat() {
        return fOp1 * fOp2;
    }

    @Benchmark
    public double measureMultDouble() {
        return dOp1 * dOp2;
    }

    @Benchmark
    public BigDecimal measureMultBigDecimal() {
        return bdOp1.multiply(bdOp2);
    }

    /*
     * Division
     */

    @Benchmark
    public float measureDivFloat() {
        return fOp1 / fOp2;
    }

    @Benchmark
    public double measureDivDouble() {
        return dOp1 / dOp2;
    }

    @Benchmark
    public BigDecimal measureDivBigDecimal() {
        return bdOp1.divide(bdOp2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /*
     * All
     */

    @Benchmark
    public float measureAllFloat() {
        return fOp1 * fOp2 + fOp1 / fOp2 - fOp2 / fOp1 + fOp2 * fOp1;
    }

    @Benchmark
    public double measureAllDouble() {
        return dOp1 * dOp2 + dOp1 / dOp2 - dOp2 / dOp1 + dOp2 * dOp1;
    }

    @Benchmark
    public BigDecimal measureAllBigDecimal() {
        return bdOp1.multiply(bdOp2)
                .add(bdOp1.divide(bdOp2,scale, BigDecimal.ROUND_HALF_UP))
                .subtract(bdOp2.divide(bdOp1, scale, BigDecimal.ROUND_HALF_UP))
                .add(bdOp2.multiply(bdOp1));
    }


}
