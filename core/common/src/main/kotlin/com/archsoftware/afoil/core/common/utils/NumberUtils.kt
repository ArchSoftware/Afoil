package com.archsoftware.afoil.core.common.utils

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols

fun Number.format(digits: Int): String = DecimalFormat(
    /*pattern = */ "0.${"0".repeat(digits)}",
    /* symbols = */ DecimalFormatSymbols.getInstance().apply {
        decimalSeparator = '.'
    }
).format(this)