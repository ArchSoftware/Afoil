package com.archsoftware.afoil.core.data.model

import com.archsoftware.afoil.core.database.model.AirfoilAnalysisProjectEntity
import com.archsoftware.afoil.core.model.AirfoilAnalysisProject

fun AirfoilAnalysisProject.asEntity(): AirfoilAnalysisProjectEntity =
    AirfoilAnalysisProjectEntity(
        name = name
    )