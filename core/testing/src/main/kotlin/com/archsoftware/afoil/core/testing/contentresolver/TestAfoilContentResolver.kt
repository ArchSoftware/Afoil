package com.archsoftware.afoil.core.testing.contentresolver

import android.net.Uri
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import org.jetbrains.annotations.TestOnly
import java.io.InputStream
import java.io.OutputStream

@TestOnly
class TestAfoilContentResolver : UriContentResolver {
    var exists: Boolean = false
    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null

    override fun checkIfUriExists(uri: Uri): Boolean = exists

    override fun checkIfTreeUriExists(treeUri: Uri): Boolean = checkIfUriExists(treeUri)

    override fun openInputStream(uri: Uri): InputStream? = inputStream

    override fun openOutputStream(uri: Uri): OutputStream? = outputStream

    override fun createDocument(
        parentDocumentUri: Uri,
        mimeType: String,
        displayName: String
    ): Uri? = testUri

    override fun deleteDocument(uri: Uri) {}

    override fun findDocument(treeUri: Uri, displayName: String): Uri? = testUri

    override fun takePersistableUriPermission(uri: Uri) {}

    override fun getDisplayName(uri: Uri): String? = null

    companion object {
        val testUri: Uri = Uri.parse("content://test_uri")
    }
}