package com.espressoprogrammer.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class StringManipulation {

    String user = "User";

    String product = "Product";

    int qty = 1;

    @Benchmark
    public void baseline() {
    }

    @Benchmark
    public String stringConcatenation() {
        return "User " + user + " bought " + qty + " items from product " + product;
    }

    @Benchmark
    public String stringBuilder() {
        return new StringBuilder("User ").append(user)
                .append(" bought ").append(qty)
                .append(" items from product ").append(product).toString();
    }

    @Benchmark
    public String stringFormat() {
        return String.format("User %s bought %d items from product %s", user, qty, product);
    }

}
