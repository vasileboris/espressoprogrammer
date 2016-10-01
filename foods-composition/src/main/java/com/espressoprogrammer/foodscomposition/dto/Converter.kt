package com.espressoprogrammer.foodscomposition.dto

import java.math.BigDecimal

fun convert(abbrev: Abbrev): AbbrevKcal = AbbrevKcal(
        abbrev.ndbNo,
        abbrev.energKcal,
        abbrev.energKcal * abbrev.gmWt1 / 100,
        abbrev.energKcal * abbrev.gmWt2 / 100)

fun complexConvert(abbrev: Abbrev): AbbrevKcal = AbbrevKcal(
        abbrev.ndbNo,
        abbrev.energKcal,
        BigDecimal(abbrev.energKcal).multiply(BigDecimal(abbrev.gmWt1)).divide(BigDecimal(100)).toDouble(),
        BigDecimal(abbrev.energKcal).multiply(BigDecimal(abbrev.gmWt2)).divide(BigDecimal(100)).toDouble())