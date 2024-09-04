package com.archsoftware.afoil.core.common.utils

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols

/**
 * Formats a [Number] to a string with the specified number of decimal places using `.` as decimal
 * separator.
 *
 * @param digits The number of decimal places to include in the formatted string.
 * @return The formatted string representation of the [Number].
 */
fun Number.format(digits: Int): String = DecimalFormat(
    /*pattern = */ "0.${"0".repeat(digits)}",
    /* symbols = */ DecimalFormatSymbols.getInstance().apply {
        decimalSeparator = '.'
    }
).format(this)