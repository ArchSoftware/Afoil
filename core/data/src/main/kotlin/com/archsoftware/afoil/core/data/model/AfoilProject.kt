package com.archsoftware.afoil.core.data.model

import com.archsoftware.afoil.core.database.model.AfoilProjectEntity
import com.archsoftware.afoil.core.model.AfoilProject

fun AfoilProject.asEntity(): AfoilProjectEntity =
    AfoilProjectEntity(
        id = id,
        name = name,
        dirUri = dirUri,
        projectDataType = projectDataType
    )