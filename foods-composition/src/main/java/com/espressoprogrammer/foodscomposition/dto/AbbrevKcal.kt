package com.espressoprogrammer.foodscomposition.dto

/**
 * It contains Kilo calories for 100g, and common house hold weights
 */
data class AbbrevKcal(val ndbNo: String,
                      val energKcal: Int,
                      val gmWt1Kcal: Double,
                      val gmWt2Kcal: Double) {

}
