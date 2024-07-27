package com.archsoftware.afoil.core.common.utils

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.snapshots.StateFactoryMarker

/**
 * Checks if the given input is a valid [Double] representation.
 */
@StateFactoryMarker
fun isValidDoubleInput(
    input: String,
    allowEmpty: Boolean = true
): State<Boolean> = derivedStateOf {
    (input.isEmpty() && allowEmpty) || input.toDoubleOrNull() != null
}

/**
 * Checks if the given input is a valid [Int] representation.
 */
@StateFactoryMarker
fun isValidIntInput(
    input: String,
    allowEmpty: Boolean = true
): State<Boolean> = derivedStateOf {
    (input.isEmpty() && allowEmpty) || input.toIntOrNull() != null
}