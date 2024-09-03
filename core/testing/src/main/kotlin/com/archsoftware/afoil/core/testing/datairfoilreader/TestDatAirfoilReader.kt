package com.archsoftware.afoil.core.testing.datairfoilreader

import android.net.Uri
import com.archsoftware.afoil.core.common.datairfoilreader.AirfoilReader
import com.archsoftware.afoil.core.common.datairfoilreader.Coordinate
import org.jetbrains.annotations.TestOnly

@TestOnly
class TestDatAirfoilReader : AirfoilReader {
    var isValid = true
    var name: String? = null

    override suspend fun checkValidity(uri: Uri?): Boolean = isValid
    override suspend fun readName(uri: Uri?): String? = name
    override suspend fun readCoordinates(uri: Uri?): List<Coordinate> = emptyList()
}