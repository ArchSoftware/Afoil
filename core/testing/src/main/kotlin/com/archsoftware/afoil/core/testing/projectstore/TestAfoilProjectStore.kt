package com.archsoftware.afoil.core.testing.projectstore

import android.net.Uri
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.projectstore.ProjectStore

class TestAfoilProjectStore : ProjectStore {
    override suspend fun createProjectDir(project: AfoilProject): Uri? = null

    override suspend fun <T> writeProjectData(uri: Uri?, projectData: T, type: Class<T>) {}

    override suspend fun <T> readProjectData(uri: Uri?, type: Class<T>): T? = null
}