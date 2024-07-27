package com.archsoftware.afoil.core.projectstore

import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.archsoftware.afoil.core.common.AfoilDispatcher
import com.archsoftware.afoil.core.common.Dispatcher
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import com.archsoftware.afoil.core.data.repository.PreferencesRepository
import com.archsoftware.afoil.core.model.AirfoilAnalysisProject
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import javax.inject.Inject

private const val PROJECT_DATA_MIME_TYPE = "application/json"
@VisibleForTesting
internal const val PROJECT_DATA_FILE_NAME = "projectData.json"

class ProjectStore @Inject constructor(
    private val contentResolver: UriContentResolver,
    private val preferencesRepository: PreferencesRepository,
    @Dispatcher(AfoilDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {
    private val gson = Gson()

    suspend fun createProjectDir(project: AirfoilAnalysisProject): Uri? {
        val projectsDirectory =
            preferencesRepository.getAfoilProjectsDirectory().first() ?: return null

        return withContext(ioDispatcher) {
            val uri = Uri.parse(projectsDirectory)
            val treeUri = DocumentsContract.buildDocumentUriUsingTree(
                /* treeUri = */ uri,
                /* documentId = */ DocumentsContract.getTreeDocumentId(uri)
            )

            var dir: Uri? = null
            try {
                dir = contentResolver.createDocument(
                    parentDocumentUri = treeUri,
                    mimeType = DocumentsContract.Document.MIME_TYPE_DIR,
                    displayName = project.name
                )
            } catch (e: FileNotFoundException) {
                Log.e("ProjectStore", "Failed to create project directory", e)
            }
            dir
        }
    }

    suspend fun <T> writeProjectData(uri: Uri?, projectData: T, type: Class<T>) {
        if (uri == null) return

        withContext(ioDispatcher) {
            try {
                val projectDataFileUri = contentResolver.createDocument(
                    parentDocumentUri = uri,
                    mimeType = PROJECT_DATA_MIME_TYPE,
                    displayName = PROJECT_DATA_FILE_NAME
                )
                requireNotNull(projectDataFileUri)
                contentResolver.openOutputStream(projectDataFileUri)
                    ?.bufferedWriter()
                    ?.use { writer ->
                        gson.toJson(projectData, type, writer)
                    }
            } catch (e: Exception) {
                Log.e("ProjectStore", "Failed to write project data", e)
            }
        }
    }

    suspend fun <T> readProjectData(uri: Uri?, type: Class<T>): T? {
        if (uri == null) return null

        val projectDataFileUri = Uri.withAppendedPath(uri, PROJECT_DATA_FILE_NAME)
        var projectData: T? = null

        return withContext(ioDispatcher) {
            try {
                contentResolver.openInputStream(projectDataFileUri)?.bufferedReader()
                    ?.use { reader ->
                        projectData = gson.fromJson(reader, type)
                    }
            } catch (e: Exception) {
                Log.e("ProjectStore", "Failed to read project data", e)
            }
            projectData
        }
    }
}