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

class AfoilContentResolver @Inject constructor(
    private val contentResolver: ContentResolver
) : UriContentResolver {

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

    override fun checkIfTreeUriExists(treeUri: Uri): Boolean {
        val uri = DocumentsContract.buildDocumentUriUsingTree(
            /* treeUri = */ treeUri,
            /* documentId = */ DocumentsContract.getTreeDocumentId(treeUri)
        )
        return checkIfUriExists(uri)
    }

    override fun openInputStream(uri: Uri): InputStream? = contentResolver.openInputStream(uri)

    override fun openOutputStream(uri: Uri): OutputStream? = contentResolver.openOutputStream(uri)

    override fun createDocument(
        parentDocumentUri: Uri,
        mimeType: String,
        displayName: String
    ): Uri? = DocumentsContract.createDocument(contentResolver, parentDocumentUri, mimeType, displayName)

    override fun deleteDocument(uri: Uri) {
        DocumentsContract.deleteDocument(contentResolver, uri)
    }

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

    override fun takePersistableUriPermission(uri: Uri) {
        contentResolver.takePersistableUriPermission(uri, TAKE_FLAGS)
    }

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