package com.espressoprogrammer.closures;

import java.util.function.Function;

public class DiscountCalculator {

    private static Function<Double, Double> calculator(String goodsType) {
        Double discount = getDiscount(goodsType);
        return v -> v * (1 - discount);
    }

    private static Double getDiscount(String goodsType) {
        Double discount = 0.1;
        if("FOOD".equals(goodsType)) {
            discount = 0.2;
        }
        return discount;
    }

    public static void main(String... args) {
        Function<Double, Double> beerDiscountCalculator = calculator("BEER");
        Function<Double, Double> foodDiscountCalculator = calculator("FOOD");

        System.out.println("BEER(100): " + beerDiscountCalculator.apply(100.0));
        System.out.println("FOOD(100): " + foodDiscountCalculator.apply(100.0));
    }

}
