package com.archsoftware.afoil.core.projectstore

import android.net.Uri
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.AfoilProjectData

interface ProjectStore {
    suspend fun createProjectDir(project: AfoilProject): Uri?
    suspend fun writeProjectData(projectDirUri: Uri?, projectData: AfoilProjectData)
    suspend fun readProjectData(projectDirUri: Uri?): AfoilProjectData?
    suspend fun deleteProject(project: AfoilProject)
}