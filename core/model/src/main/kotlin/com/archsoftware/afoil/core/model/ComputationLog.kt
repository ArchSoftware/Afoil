package com.archsoftware.afoil.core.model

import android.graphics.Color

data class ComputationLog(
    val timestamp: String,
    val tag: String,
    val message: String,
    val level: Level
) {
    enum class Level(val identifier: String, val color: Int) {
        INFO("I",Color.parseColor("#366079")),
        WARNING("W", Color.parseColor("#bbb52a")),
        ERROR("E", Color.parseColor("#cf5b56"))
    }
}
