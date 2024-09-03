package com.archsoftware.afoil.computation.manager.model

import com.archsoftware.afoil.core.common.datairfoilreader.Coordinate
import com.archsoftware.afoil.core.math.model.Point

fun Point.asExternalModel(): Coordinate = Coordinate(
    x = x,
    y = y
)