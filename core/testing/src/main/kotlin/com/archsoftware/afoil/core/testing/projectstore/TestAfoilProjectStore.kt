package com.archsoftware.afoil.core.testing.projectstore

import android.net.Uri
import com.archsoftware.afoil.core.model.ProjectData
import com.archsoftware.afoil.core.model.ProjectNumResult
import com.archsoftware.afoil.core.projectstore.ProjectStore
import org.jetbrains.annotations.TestOnly

@TestOnly
class TestAfoilProjectStore : ProjectStore {
    override suspend fun createProjectDir(name: String): Uri? = null

    override suspend fun writeProjectData(dirUri: Uri, projectData: ProjectData): Uri? = null

    override suspend fun readProjectData(uri: Uri): ProjectData? = null

    override suspend fun writeProjectNumResult(dirUri: Uri, result: ProjectNumResult): Uri? = null

    override suspend fun readProjectNumResult(uri: Uri): ProjectNumResult? = null

    override suspend fun deleteProject(dirUri: Uri) {}

    override suspend fun copyToProjectDir(dirUri: Uri, sourceUri: Uri) {}
}