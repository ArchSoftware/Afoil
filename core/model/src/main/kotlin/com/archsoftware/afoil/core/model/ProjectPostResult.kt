package com.archsoftware.afoil.core.model

import android.graphics.Bitmap

/**
 * Local representation of generic Afoil project post-processing result.
 */
sealed class ProjectPostResult {
    companion object {
        val mimeType: String = "image/png"
    }
    abstract val bitmap: Bitmap
    abstract val displayName: String
}

/**
 * Data model for streamlines plot result.
 */
data class StreamlinesPlotResult(override val bitmap: Bitmap) : ProjectPostResult() {
    override val displayName: String = "streamlinesPlot.png"
}

/**
 * Data model for pressure contours plot result.
 */
data class ContoursPlotResult(override val bitmap: Bitmap) : ProjectPostResult() {
    override val displayName: String = "contoursPlot.png"
}

/**
 * Data model for pressure plot result.
 */
data class PressurePlotResult(override val bitmap: Bitmap) : ProjectPostResult() {
    override val displayName: String = "pressurePlot.png"
}