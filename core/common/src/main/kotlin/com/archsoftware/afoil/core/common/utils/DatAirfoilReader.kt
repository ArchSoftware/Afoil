package com.archsoftware.afoil.core.common.utils

import android.net.Uri
import com.archsoftware.afoil.core.common.AfoilDispatcher
import com.archsoftware.afoil.core.common.Dispatcher
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class DatAirfoilReader @Inject constructor(
    private val contentResolver: UriContentResolver,
    @Dispatcher(AfoilDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {

    @Throws(IOException::class)
    suspend fun readName(uri: Uri?): String? {
        var line: String? = null
        return withContext(ioDispatcher) {
            if (uri != null) {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    inputStream.bufferedReader().use { reader ->
                        line = reader.readLine()
                    }
                }
            }
            line
        }
    }
}