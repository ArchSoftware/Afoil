package com.archsoftware.afoil.core.projectstore

import android.net.Uri
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.AfoilProjectData

interface ProjectStore {
    suspend fun createProjectDir(project: AfoilProject)
    suspend fun setProjectDir(project: AfoilProject)
    suspend fun writeProjectData(projectData: AfoilProjectData)
    suspend fun readProjectData(): AfoilProjectData?
    suspend fun deleteProject(project: AfoilProject)
    suspend fun copyToProjectDir(sourceUri: Uri?)
}