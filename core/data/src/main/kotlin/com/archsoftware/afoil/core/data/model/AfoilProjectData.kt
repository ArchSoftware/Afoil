package com.archsoftware.afoil.core.data.model

import com.archsoftware.afoil.core.database.model.AfoilProjectDataEntity
import com.archsoftware.afoil.core.model.AfoilProjectData

fun AfoilProjectData.asEntity(): AfoilProjectDataEntity =
    AfoilProjectDataEntity(
        id = id,
        uri = uri,
        projectOwnerId = projectOwnerId
    )