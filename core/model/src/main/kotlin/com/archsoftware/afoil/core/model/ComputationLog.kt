package com.archsoftware.afoil.core.model

import android.graphics.Color

/**
 * Data model for a computation log entry.
 */
data class ComputationLog(
    val timestamp: String,
    val tag: String,
    val message: String,
    val level: Level
) {
    /**
     * Represents a computation log event level.
     */
    enum class Level(val identifier: String, val color: Int) {
        INFO("I",Color.parseColor("#366079")),
        WARNING("W", Color.parseColor("#bbb52a")),
        ERROR("E", Color.parseColor("#cf5b56"))
    }
}
