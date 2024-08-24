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

    override fun checkIfTreeUriExists(uri: Uri): Boolean {
        val treeUri = DocumentsContract.buildDocumentUriUsingTree(
            /* treeUri = */ uri,
            /* documentId = */ DocumentsContract.getTreeDocumentId(uri)
        )
        return checkIfUriExists(treeUri)
    }

    override fun openInputStream(uri: Uri): InputStream? = contentResolver.openInputStream(uri)

    override fun openOutputStream(uri: Uri): OutputStream? = contentResolver.openOutputStream(uri)

    override fun createDocument(
        parentDocumentUri: Uri,
        mimeType: String,
        displayName: String
    ): Uri? = DocumentsContract.createDocument(contentResolver, parentDocumentUri, mimeType, displayName)

    override fun takePersistableUriPermission(uri: Uri) {
        contentResolver.takePersistableUriPermission(uri, TAKE_FLAGS)
    }
}