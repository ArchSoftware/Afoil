package com.archsoftware.afoil.core.projectstore

import android.graphics.Bitmap
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

/**
 * Implementation of the [ProjectStore] interface.
 *
 * Provides utility method to create and manage project directories, read and write project data
 * and results.
 */
@OptIn(ExperimentalSerializationApi::class)
class AfoilProjectStore @Inject constructor(
    private val contentResolver: UriContentResolver,
    private val preferencesRepository: PreferencesRepository,
    @Dispatcher(AfoilDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : ProjectStore {
    /**
     * Creates a new project directory with the given name.
     *
     * @param name The name of the project directory to create.
     * @return The [Uri] of the newly created project directory or `null` if an error occurred.
     */
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

    /**
     * Writes the given project data to the target project directory.
     *
     * @param dirUri The uri of the target project directory.
     * @param projectData The data to write.
     * @return The [Uri] of the newly created project data file or `null` if an error occurred.
     */
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

    /**
     * Reads the project data from the given [Uri].
     *
     * @param uri The uri of the project data file.
     * @return The [ProjectData] or `null` if an error occurred.
     */
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

    /**
     * Writes the given project numerical result to the target project directory.
     *
     * @param dirUri The uri of the target project directory.
     * @param result The numerical result to write.
     * @return The [Uri] of the newly created project numerical result file or `null` if an error
     * occurred.
     */
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

    /**
     * Reads the project numerical result from the given [Uri].
     *
     * @param uri The uri of the project numerical result file.
     * @return The [ProjectNumResult] or `null` if an error occurred.
     */
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

    /**
     * Writes the given post-processing result to the target project directory.
     *
     * @param dirUri The uri of the target project directory.
     * @param result The post-processing result to write.
     * @return The [Uri] of the newly created post-processing result file or `null` if an error
     * occurred.
     */
    override suspend fun writeProjectPostResult(dirUri: Uri, result: ProjectPostResult): Uri? {
        var postResultFileUri: Uri? = null
        withContext(ioDispatcher) {
            try {
                val uri = contentResolver.createDocument(
                    parentDocumentUri = dirUri,
                    mimeType = ProjectPostResult.mimeType,
                    displayName = result.displayName
                )
                requireNotNull(uri)
                contentResolver.openOutputStream(uri)
                    ?.use { outputStream ->
                        result.bitmap.compress(
                            /* format = */ Bitmap.CompressFormat.PNG,
                            /* quality = */ 100,
                            /* stream = */ outputStream
                        )
                    }
                postResultFileUri = uri
            } catch (e: Exception) {
                Log.e("ProjectStore", "Failed to write post result", e)
            }
        }
        return postResultFileUri
    }

    /**
     * Deletes the given project directory.
     *
     * @param dirUri The uri of the project directory to delete.
     */
    override suspend fun deleteProject(dirUri: Uri) {
        withContext(ioDispatcher) {
            try {
                contentResolver.deleteDocument(dirUri)
            } catch (e: FileNotFoundException) {
                Log.e("ProjectStore", "Failed to delete project directory", e)
            }
        }
    }

    /**
     * Copies the given file to the target project directory.
     *
     * @param dirUri The uri of the target project directory.
     * @param sourceUri The uri of the file to copy.
     */
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