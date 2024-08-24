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
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            return it.count > 0
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
            /* parentDocumentId = */ DocumentsContract.getTreeDocumentId(treeUri)
        )
        val cursor = contentResolver.query(
            /* uri = */ childrenUri,
            /* projection = */ null,
            /* selection = */ "${DocumentsContract.Document.COLUMN_DISPLAY_NAME} = ?",
            /* selectionArgs = */ arrayOf(displayName),
            /* sortOrder = */ null
        )
        cursor?.use {
            it.moveToFirst()
            val documentId = it.getString(it.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DOCUMENT_ID))
            return DocumentsContract.buildDocumentUriUsingTree(
                /* treeUri = */ treeUri,
                /* documentId = */ documentId
            )
        }
        return null
    }

    override fun takePersistableUriPermission(uri: Uri) {
        contentResolver.takePersistableUriPermission(uri, TAKE_FLAGS)
    }
}