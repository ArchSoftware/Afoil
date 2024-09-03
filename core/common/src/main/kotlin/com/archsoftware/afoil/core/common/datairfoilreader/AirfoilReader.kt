package com.archsoftware.afoil.core.common.datairfoilreader

import android.net.Uri

interface AirfoilReader {
    suspend fun checkValidity(uri: Uri?): Boolean
    suspend fun readName(uri: Uri?): String?
    suspend fun readCoordinates(uri: Uri?): List<Coordinate>
}