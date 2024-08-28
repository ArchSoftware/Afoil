package com.archsoftware.afoil.core.projectstore

import android.net.Uri
import com.archsoftware.afoil.core.model.ProjectData
import com.archsoftware.afoil.core.model.ProjectNumResult

interface ProjectStore {
    suspend fun createProjectDir(name: String): Uri?
    suspend fun writeProjectData(dirUri: Uri, projectData: ProjectData): Uri?
    suspend fun readProjectData(dirUri: Uri): ProjectData?
    suspend fun writeProjectNumResult(dirUri: Uri, result: ProjectNumResult): Uri?
    suspend fun readProjectNumResult(dirUri: Uri): ProjectNumResult?
    suspend fun deleteProject(dirUri: Uri)
    suspend fun copyToProjectDir(dirUri: Uri, sourceUri: Uri)
}