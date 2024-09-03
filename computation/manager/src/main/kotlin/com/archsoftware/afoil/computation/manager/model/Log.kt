package com.archsoftware.afoil.computation.manager.model

import com.archsoftware.afoil.core.model.ComputationLog
import com.archsoftware.afoil.engine.logger.Log

fun Log.asExternalModel(): ComputationLog = ComputationLog(
    timestamp = timestamp,
    tag = tag,
    message = msg,
    level = level.asExternalModel()
)

fun Log.Level.asExternalModel(): ComputationLog.Level = when (this) {
    Log.Level.INFO -> ComputationLog.Level.INFO
    Log.Level.WARNING -> ComputationLog.Level.WARNING
    Log.Level.ERROR -> ComputationLog.Level.ERROR
}