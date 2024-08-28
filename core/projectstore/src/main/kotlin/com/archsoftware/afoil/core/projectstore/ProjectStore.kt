package com.archsoftware.afoil.core.projectstore

import android.net.Uri
import com.archsoftware.afoil.core.model.ProjectData

interface ProjectStore {
    suspend fun createProjectDir(name: String): Uri?
    suspend fun writeProjectData(dirUri: Uri, projectData: ProjectData): Uri?
    suspend fun readProjectData(dirUri: Uri): ProjectData?
    suspend fun deleteProject(dirUri: Uri)
    suspend fun copyToProjectDir(dirUri: Uri, sourceUri: Uri)
}