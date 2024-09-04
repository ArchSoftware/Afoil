package com.archsoftware.afoil.core.model

import android.graphics.Bitmap

/**
 * Local representation of generic Afoil project post-processing result.
 */
sealed class ProjectPostResult {
    companion object {
        val mimeType: String = "image/png"
    }
    abstract val displayName: String
}

/**
 * Data model for streamlines plot.
 */
data class StreamlinesPlot(val bitmap: Bitmap) : ProjectPostResult() {
    override val displayName: String = "streamlinesPlot.png"
}

/**
 * Data model for pressure contours plot.
 */
data class ContoursPlot(val bitmap: Bitmap) : ProjectPostResult() {
    override val displayName: String = "contoursPlot.png"
}

/**
 * Data model for pressure plot.
 */
data class PressurePlot(val bitmap: Bitmap) : ProjectPostResult() {
    override val displayName: String = "pressurePlot.png"
}