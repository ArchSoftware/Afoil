package com.archsoftware.afoil.core.projectstore

import android.net.Uri
import com.archsoftware.afoil.core.model.AfoilProject

interface ProjectStore {
    suspend fun createProjectDir(project: AfoilProject): Uri?
    suspend fun <T> writeProjectData(uri: Uri?, projectData: T, type: Class<T>)
    suspend fun <T> readProjectData(uri: Uri?, type: Class<T>): T?
}