package com.archsoftware.afoil.core.testing.projectstore

import android.net.Uri
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.AfoilProjectData
import com.archsoftware.afoil.core.projectstore.ProjectStore
import org.jetbrains.annotations.TestOnly

@TestOnly
class TestAfoilProjectStore : ProjectStore {
    override suspend fun createProjectDir(project: AfoilProject): Uri? = null

    override suspend fun writeProjectData(projectDirUri: Uri?, projectData: AfoilProjectData) {}

    override suspend fun readProjectData(projectDirUri: Uri?): AfoilProjectData? = null

    override suspend fun deleteProject(project: AfoilProject) {}
}