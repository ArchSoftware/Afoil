package com.archsoftware.afoil.core.projectstore

import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.archsoftware.afoil.core.common.AfoilDispatcher
import com.archsoftware.afoil.core.common.Dispatcher
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import com.archsoftware.afoil.core.data.repository.PreferencesRepository
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.AfoilProjectData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.FileNotFoundException
import javax.inject.Inject

private const val PROJECT_DATA_MIME_TYPE = "application/json"
@VisibleForTesting
internal const val PROJECT_DATA_FILE_NAME = "projectData.json"

@OptIn(ExperimentalSerializationApi::class)
class AfoilProjectStore @Inject constructor(
    private val contentResolver: UriContentResolver,
    private val preferencesRepository: PreferencesRepository,
    @Dispatcher(AfoilDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : ProjectStore {

    override suspend fun createProjectDir(project: AfoilProject): Uri? {
        val projectsDirectory =
            preferencesRepository.getAfoilProjectsDirectory().first() ?: return null

        return withContext(ioDispatcher) {
            val treeUri = Uri.parse(projectsDirectory)
            val uri = DocumentsContract.buildDocumentUriUsingTree(
                /* treeUri = */ treeUri,
                /* documentId = */ DocumentsContract.getTreeDocumentId(treeUri)
            )

            var dir: Uri? = null
            try {
                dir = contentResolver.createDocument(
                    parentDocumentUri = uri,
                    mimeType = DocumentsContract.Document.MIME_TYPE_DIR,
                    displayName = project.name
                )
            } catch (e: FileNotFoundException) {
                Log.e("ProjectStore", "Failed to create project directory", e)
            }
            dir
        }
    }

    override suspend fun writeProjectData(projectDirUri: Uri?, projectData: AfoilProjectData) {
        if (projectDirUri == null) return

        withContext(ioDispatcher) {
            try {
                val projectDataFileUri = contentResolver.createDocument(
                    parentDocumentUri = projectDirUri,
                    mimeType = PROJECT_DATA_MIME_TYPE,
                    displayName = PROJECT_DATA_FILE_NAME
                )
                requireNotNull(projectDataFileUri)
                contentResolver.openOutputStream(projectDataFileUri)
                    ?.use { outputStream ->
                        Json.encodeToStream(projectData, outputStream)
                    }
            } catch (e: Exception) {
                Log.e("ProjectStore", "Failed to write project data", e)
            }
        }
    }

    override suspend fun readProjectData(projectDirUri: Uri?): AfoilProjectData? {
        if (projectDirUri == null) return null

        val projectDataFileUri = Uri.withAppendedPath(projectDirUri, PROJECT_DATA_FILE_NAME)
        var projectData: AfoilProjectData? = null

        return withContext(ioDispatcher) {
            try {
                contentResolver.openInputStream(projectDataFileUri)?.use { inputStream ->
                    projectData = Json.decodeFromStream(inputStream)
                }
            } catch (e: Exception) {
                Log.e("ProjectStore", "Failed to read project data", e)
            }
            projectData
        }
    }

    override suspend fun deleteProject(project: AfoilProject) {
        val projectsDirectory =
            preferencesRepository.getAfoilProjectsDirectory().first() ?: return

        withContext(ioDispatcher) {
            val treeUri = Uri.parse(projectsDirectory)
            val projectDirUri =
                contentResolver.findDocument(treeUri, project.name) ?: return@withContext

            try {
                contentResolver.deleteDocument(projectDirUri)
            } catch (e: FileNotFoundException) {
                Log.e("ProjectStore", "Failed to delete project directory", e)
            }
        }
    }
}