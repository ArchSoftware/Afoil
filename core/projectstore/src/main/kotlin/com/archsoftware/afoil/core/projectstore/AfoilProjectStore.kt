package com.archsoftware.afoil.core.projectstore

import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.core.net.toUri
import com.archsoftware.afoil.core.common.AfoilDispatcher
import com.archsoftware.afoil.core.common.Dispatcher
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import com.archsoftware.afoil.core.data.repository.PreferencesRepository
import com.archsoftware.afoil.core.model.ProjectData
import com.archsoftware.afoil.core.model.ProjectNumResult
import com.archsoftware.afoil.core.model.ProjectPostResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.FileNotFoundException
import javax.inject.Inject

private const val GENERIC_MIME_TYPE = "*/*"

@OptIn(ExperimentalSerializationApi::class)
class AfoilProjectStore @Inject constructor(
    private val contentResolver: UriContentResolver,
    private val preferencesRepository: PreferencesRepository,
    @Dispatcher(AfoilDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : ProjectStore {
    override suspend fun createProjectDir(name: String): Uri? {
        val projectsDirectory =
            preferencesRepository.getAfoilProjectsDirectory().first() ?: return null

        var projectDirUri: Uri? = null
        withContext(ioDispatcher) {
            val treeUri = projectsDirectory.toUri()
            val uri = DocumentsContract.buildDocumentUriUsingTree(
                /* treeUri = */ treeUri,
                /* documentId = */ DocumentsContract.getTreeDocumentId(treeUri)
            )

            try {
                projectDirUri = contentResolver.createDocument(
                    parentDocumentUri = uri,
                    mimeType = DocumentsContract.Document.MIME_TYPE_DIR,
                    displayName = name
                )
            } catch (e: FileNotFoundException) {
                Log.e("ProjectStore", "Failed to create project directory", e)
            }
        }
        return projectDirUri
    }

    override suspend fun writeProjectData(dirUri: Uri, projectData: ProjectData): Uri? {
        var projectDataFileUri: Uri? = null
        withContext(ioDispatcher) {
            try {
                val uri = contentResolver.createDocument(
                    parentDocumentUri = dirUri,
                    mimeType = ProjectData.mimeType,
                    displayName = ProjectData.displayName
                )
                requireNotNull(uri)
                contentResolver.openOutputStream(uri)
                    ?.use { outputStream ->
                        Json.encodeToStream(projectData, outputStream)
                    }
                projectDataFileUri = uri
            } catch (e: Exception) {
                Log.e("ProjectStore", "Failed to write project data", e)
            }
        }
        return projectDataFileUri
    }

    override suspend fun readProjectData(uri: Uri): ProjectData? {
        var projectData: ProjectData? = null

        return withContext(ioDispatcher) {
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    projectData = Json.decodeFromStream(inputStream)
                }
            } catch (e: Exception) {
                Log.e("ProjectStore", "Failed to read project data", e)
            }
            projectData
        }
    }

    override suspend fun writeProjectNumResult(dirUri: Uri, result: ProjectNumResult): Uri? {
        var projectNumResultFileUri: Uri? = null
        withContext(ioDispatcher) {
            try {
                val uri = contentResolver.createDocument(
                    parentDocumentUri = dirUri,
                    mimeType = ProjectNumResult.mimeType,
                    displayName = ProjectNumResult.displayName
                )
                requireNotNull(uri)
                contentResolver.openOutputStream(uri)
                    ?.use { outputStream ->
                        Json.encodeToStream(result, outputStream)
                    }
                projectNumResultFileUri = uri
            } catch (e: Exception) {
                Log.e("ProjectStore", "Failed to write project result", e)
            }
        }
        return projectNumResultFileUri
    }

    override suspend fun readProjectNumResult(uri: Uri): ProjectNumResult? {
        var projectNumResult: ProjectNumResult? = null
        withContext(ioDispatcher) {
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    projectNumResult = Json.decodeFromStream(inputStream)
                }
            } catch (e: Exception) {
                Log.e("ProjectStore", "Failed to read project result", e)
            }
        }
        return projectNumResult
    }

    override suspend fun writeProjectPostResult(dirUri: Uri, result: ProjectPostResult): Uri? {
        var postResultFileUri: Uri? = null
        withContext(ioDispatcher) {
            try {
                postResultFileUri = contentResolver.createDocument(
                    parentDocumentUri = dirUri,
                    mimeType = ProjectPostResult.mimeType,
                    displayName = result.displayName
                )
            } catch (e: Exception) {
                Log.e("ProjectStore", "Failed to write post result", e)
            }
        }
        return postResultFileUri
    }

    override suspend fun deleteProject(dirUri: Uri) {
        withContext(ioDispatcher) {
            try {
                contentResolver.deleteDocument(dirUri)
            } catch (e: FileNotFoundException) {
                Log.e("ProjectStore", "Failed to delete project directory", e)
            }
        }
    }

    override suspend fun copyToProjectDir(dirUri: Uri, sourceUri: Uri) {
        val displayName = contentResolver.getDisplayName(sourceUri) ?: return

        withContext(ioDispatcher) {
            try {
                val destinationUri = contentResolver.createDocument(
                    parentDocumentUri = dirUri,
                    mimeType = GENERIC_MIME_TYPE,
                    displayName = displayName
                )
                requireNotNull(destinationUri)
                contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                    contentResolver.openOutputStream(destinationUri)?.use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            } catch (e: Exception) {
                Log.e("ProjectStore", "Failed to copy file", e)
            }
        }
    }
}