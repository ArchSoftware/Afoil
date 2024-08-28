package com.archsoftware.afoil.core.data.model

import com.archsoftware.afoil.core.database.model.AfoilProjectNumResultEntity
import com.archsoftware.afoil.core.model.AfoilProjectNumResult

fun AfoilProjectNumResult.asEntity(): AfoilProjectNumResultEntity =
    AfoilProjectNumResultEntity(
        id = id,
        uri = uri,
        projectOwnerId = projectOwnerId
    )