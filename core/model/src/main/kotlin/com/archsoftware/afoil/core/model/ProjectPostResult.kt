package com.archsoftware.afoil.core.model

import android.graphics.Bitmap

sealed class ProjectPostResult {
    companion object {
        val mimeType: String = "image/png"
    }
    abstract val displayName: String
}

data class StreamlinesPlot(val bitmap: Bitmap) : ProjectPostResult() {
    override val displayName: String = "streamlinesPlot.png"
}

data class ContoursPlot(val bitmap: Bitmap) : ProjectPostResult() {
    override val displayName: String = "contoursPlot.png"
}

data class PressurePlot(val bitmap: Bitmap) : ProjectPostResult() {
    override val displayName: String = "pressurePlot.png"
}