package com.archsoftware.afoil.core.data.model

import com.archsoftware.afoil.core.database.model.AfoilProjectPostResultEntity
import com.archsoftware.afoil.core.model.AfoilProjectPostResult

fun AfoilProjectPostResult.asEntity(): AfoilProjectPostResultEntity =
    AfoilProjectPostResultEntity(
        id = id,
        nameId = nameId,
        uri = uri,
        projectOwnerId = projectOwnerId
    )