package com.archsoftware.afoil.core.common.contentresolver

import android.net.Uri
import java.io.InputStream
import java.io.OutputStream

interface UriContentResolver {
    fun checkIfUriExists(uri: Uri): Boolean
    fun checkIfTreeUriExists(treeUri: Uri): Boolean
    fun openInputStream(uri: Uri): InputStream?
    fun openOutputStream(uri: Uri): OutputStream?
    fun createDocument(
        parentDocumentUri: Uri,
        mimeType: String,
        displayName: String
    ): Uri?
    fun deleteDocument(uri: Uri)
    fun findDocument(treeUri: Uri, displayName: String): Uri?
    fun takePersistableUriPermission(uri: Uri)
    fun getDisplayName(uri: Uri): String?
}