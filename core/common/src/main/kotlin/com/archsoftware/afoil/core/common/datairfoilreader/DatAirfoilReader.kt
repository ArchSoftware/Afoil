package com.archsoftware.afoil.core.common.datairfoilreader

import android.net.Uri
import com.archsoftware.afoil.core.common.AfoilDispatcher
import com.archsoftware.afoil.core.common.Dispatcher
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.stream.consumeAsFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of the [AirfoilReader] interface.
 *
 * Provides utility methods to read airfoil details from a Selig format dat file.
 */
class DatAirfoilReader @Inject constructor(
    private val contentResolver: UriContentResolver,
    @Dispatcher(AfoilDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : AirfoilReader {

    /**
     * Checks if the given [Uri] is a valid dat file.
     *
     * @param uri The [Uri] to check.
     * @return `true` if the [Uri] is a valid dat file, `false` otherwise.
     * @throws IOException If an error occurs while reading the [Uri].
     */
    @Throws(IOException::class)
    override suspend fun checkValidity(uri: Uri?): Boolean {
        return withContext(ioDispatcher) {
            if (uri != null) {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    inputStream.bufferedReader().use { reader ->
                        reader.readLine() // Skip airfoil name line
                        val firstCoordinatesLine = reader.readLine()
                        val firstParts = firstCoordinatesLine.split("\\s+".toRegex()).filter {
                            it.toDoubleOrNull() != null
                        }
                        if (firstParts.size != 2) return@withContext false
                        val xFirst = firstParts[0].toDouble()
                        if (xFirst != 1.0) return@withContext false
                        val lastCoordinatesLine = reader.lines().consumeAsFlow().last()
                        val lastParts = lastCoordinatesLine.split("\\s+".toRegex()).filter {
                            it.toDoubleOrNull() != null
                        }
                        if (lastParts.size != 2) return@withContext false
                        val xLast = lastParts[0].toDouble()
                        if (xLast != 1.0) return@withContext false
                    }
                }
            }
            return@withContext true
        }
    }

    /**
     * Read the name of the airfoil from the given [Uri].
     *
     * @param uri The [Uri] of the dat file to read the name from.
     * @return The name of the airfoil, or `null` if provided [Uri] is invalid.
     * @throws IOException If an error occurs while reading the [Uri].
     */
    @Throws(IOException::class)
    override suspend fun readName(uri: Uri?): String? {
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

    /**
     * Read the coordinates of the airfoil from the given [Uri].
     *
     * @param uri The [Uri] of the dat file to read the coordinates from.
     * @return A list of [Coordinate] objects representing the airfoil coordinates.
     * @throws IOException If an error occurs while reading the [Uri].
     */
    @Throws(IOException::class)
    override suspend fun readCoordinates(uri: Uri?): List<Coordinate> {
        val coordinates = mutableListOf<Coordinate>()
        withContext(ioDispatcher) {
            if (uri != null) {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    inputStream.bufferedReader().use { reader ->
                        reader.readLine() // Skip airfoil name line
                        var line = reader.readLine()
                        while (line != null) {
                            val parts = line.split("\\s+".toRegex()).filter {
                                it.toDoubleOrNull() != null
                            }
                            if (parts.size == 2) {
                                val x = parts[0].toDouble()
                                val y = parts[1].toDouble()
                                coordinates.add(Coordinate(x, y))
                            }
                            line = reader.readLine()
                        }
                    }
                }
            }
        }
        return coordinates
    }
}