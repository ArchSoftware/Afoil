package com.archsoftware.afoil.core.testing.projectstore

import android.net.Uri
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.AfoilProjectData
import com.archsoftware.afoil.core.projectstore.ProjectStore
import org.jetbrains.annotations.TestOnly

@TestOnly
class TestAfoilProjectStore : ProjectStore {
    override suspend fun createProjectDir(project: AfoilProject) {}

    override suspend fun setProjectDir(project: AfoilProject) {}

    override suspend fun writeProjectData(projectData: AfoilProjectData) {}

    override suspend fun readProjectData(): AfoilProjectData? = null

    override suspend fun deleteProject(project: AfoilProject) {}

    override suspend fun copyToProjectDir(sourceUri: Uri?) {}
}