package com.espressoprogrammer.foodscomposition.dto

/**
 * It contains values from ABBREV.txt file
 */
data class Abbrev(val ndbNo: String,
                  val shrtDesc: String,
                  val energKcal: Int) {

    var gmWt1 : Double = 0.0;
    var gmWtDesc1 : String = "";
    var gmWt2 : Double = 0.0;
    var gmWtDesc2 : String = "";

}
