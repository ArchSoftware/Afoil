package com.archsoftware.afoil.core.common.contentresolver

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

private const val TAKE_FLAGS: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
        Intent.FLAG_GRANT_WRITE_URI_PERMISSION

/**
 * Implementation of the [UriContentResolver] interface.
 *
 * Provides utility methods to simplify interactions between [Uri]s and [ContentResolver].
 */
class AfoilContentResolver @Inject constructor(
    private val contentResolver: ContentResolver
) : UriContentResolver {

    /**
     * Checks if the given [Uri] exists.
     *
     * @param uri The [Uri] to check.
     * @return `true` if the [Uri] exists, `false` otherwise.
     */
    override fun checkIfUriExists(uri: Uri): Boolean {
        try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                return it.count > 0
            }
        } catch (e: SecurityException) {
            return false
        }
        return false
    }

    /**
     * Checks if the given tree [Uri] exists.
     *
     * @param treeUri The tree [Uri] to check.
     * @return `true` if the tree exists, `false` otherwise.
     */
    override fun checkIfTreeUriExists(treeUri: Uri): Boolean {
        val uri = DocumentsContract.buildDocumentUriUsingTree(
            /* treeUri = */ treeUri,
            /* documentId = */ DocumentsContract.getTreeDocumentId(treeUri)
        )
        return checkIfUriExists(uri)
    }

    /**
     * Open a stream on to the content associated with a content [Uri].
     *
     * @see ContentResolver.openInputStream
     */
    override fun openInputStream(uri: Uri): InputStream? = contentResolver.openInputStream(uri)

    /**
     * Open a stream on to the content associated with a content [Uri].
     *
     * @see ContentResolver.openOutputStream
     */
    override fun openOutputStream(uri: Uri): OutputStream? = contentResolver.openOutputStream(uri)

    /**
     * Create a new document with given [mimeType] and [displayName] under the given [parentDocumentUri].
     *
     * @see DocumentsContract.createDocument
     */
    override fun createDocument(
        parentDocumentUri: Uri,
        mimeType: String,
        displayName: String
    ): Uri? = DocumentsContract.createDocument(contentResolver, parentDocumentUri, mimeType, displayName)

    /**
     * Delete the document at the given [uri].
     *
     * @see DocumentsContract.deleteDocument
     */
    override fun deleteDocument(uri: Uri) {
        DocumentsContract.deleteDocument(contentResolver, uri)
    }

    /**
     * Find a document with the given [displayName] under the given [treeUri].
     *
     * @param treeUri The tree [Uri] under which to search for the document.
     * @param displayName The name of the document to find.
     * @return The [Uri] of the found document, or `null` if not found or an error occurred.
     */
    override fun findDocument(treeUri: Uri, displayName: String): Uri? {
        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
            /* treeUri = */ treeUri,
            /* parentDocumentId = */ DocumentsContract.getDocumentId(treeUri)
        )
        try {
            val cursor = contentResolver.query(
                /* uri = */ childrenUri,
                /* projection = */
                arrayOf(DocumentsContract.Document.COLUMN_DOCUMENT_ID),
                /* selection = */
                "${DocumentsContract.Document.COLUMN_DISPLAY_NAME} = $displayName",
                /* selectionArgs = */
                arrayOf(displayName),
                /* sortOrder = */
                null
            )
            cursor?.use {
                if (it.moveToFirst()) {
                    val documentId =
                        it.getString(it.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DOCUMENT_ID))
                    return DocumentsContract.buildDocumentUriUsingTree(
                        /* treeUri = */ treeUri,
                        /* documentId = */ documentId
                    )
                }
            }
        } catch (e: SecurityException) {
            return null
        }
        return null
    }

    /**
     * Take persistable read/write permissions for the given [Uri].
     *
     * @see ContentResolver.takePersistableUriPermission
     */
    override fun takePersistableUriPermission(uri: Uri) {
        contentResolver.takePersistableUriPermission(uri, TAKE_FLAGS)
    }

    /**
     * Get the display name of the document at the given [Uri].
     *
     * @param uri The [Uri] of the document.
     * @return The display name of the document, or `null` if not found or an error occurred.
     */
    override fun getDisplayName(uri: Uri): String? {
        try {
            val cursor = contentResolver.query(
                /* uri = */ uri,
                /* projection = */ arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME),
                /* selection = */ null,
                /* selectionArgs = */ null,
                /* sortOrder = */ null
            )
            cursor?.use {
                if (it.moveToFirst()) {
                    return it.getString(it.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DISPLAY_NAME))
                }
            }
        } catch (e: SecurityException) {
            return null
        }
        return null
    }
}