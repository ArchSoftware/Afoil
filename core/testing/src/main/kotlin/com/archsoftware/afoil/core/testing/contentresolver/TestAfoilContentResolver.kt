package com.archsoftware.afoil.core.testing.contentresolver

import android.net.Uri
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import org.jetbrains.annotations.TestOnly
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@TestOnly
class TestAfoilContentResolver @Inject constructor() : UriContentResolver {
    var exists: Boolean = false
    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null

    val testUri: Uri? = Uri.parse("content://test_uri")

    override fun checkIfUriExists(uri: Uri): Boolean = exists

    override fun checkIfTreeUriExists(uri: Uri): Boolean = checkIfUriExists(uri)

    override fun openInputStream(uri: Uri): InputStream? = inputStream

    override fun openOutputStream(uri: Uri): OutputStream? = outputStream

    override fun createDocument(
        parentDocumentUri: Uri,
        mimeType: String,
        displayName: String
    ): Uri? = testUri

    override fun takePersistableUriPermission(uri: Uri) {}
}