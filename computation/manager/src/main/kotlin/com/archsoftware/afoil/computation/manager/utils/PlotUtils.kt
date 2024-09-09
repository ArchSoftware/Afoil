package com.archsoftware.afoil.computation.manager.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import com.archsoftware.afoil.core.math.model.Point
import com.archsoftware.afoil.engine.model.ComputationalField
import com.archsoftware.afoil.engine.model.Streamline

private const val DEFAULT_PLOT_WIDTH = 1080
private const val DEFAULT_PLOT_HEIGHT = 1080
private const val DEFAULT_BACKGROUND_COLOR = Color.WHITE
private const val DEFAULT_STROKE_COLOR = Color.BLACK
private const val DEFAULT_FILL_COLOR = Color.BLACK

/**
 * Converts a list of [Point]s to a [Path].
 *
 * @param path The [Path] to add the points to (for multiple requests pass the same path object
 * and call [Path.reset] before each call). Defaults to a new [Path].
 * @param autoClose Whether to close the path after adding all points. Defaults to `true`.
 * @return The [Path] containing the points.
 */
fun List<Point>.toPath(path: Path = Path(), autoClose: Boolean = true): Path {
    path.moveTo(first().x.toFloat(), first().y.toFloat())
    drop(1).forEach {
        path.lineTo(it.x.toFloat(), it.y.toFloat())
    }
    if (autoClose) path.close()
    return path
}

/**
 * Generates a bitmap of the streamlines plot.
 *
 * @param computationalField The computational field to use for the plot.
 * @param airfoilPoints The points of the airfoil to use for the plot.
 * @param width The width of the bitmap. Defaults to `1080`.
 * @param height The height of the bitmap. Defaults to `1080`.
 * @return The bitmap of the streamlines plot.
 */
fun List<Streamline>.generateStreamlinesPlotBitmap(
    computationalField: ComputationalField,
    airfoilPoints: List<Point>,
    width: Int = DEFAULT_PLOT_WIDTH,
    height: Int = DEFAULT_PLOT_HEIGHT
): Bitmap {
    val fieldWidth = computationalField.right - computationalField.left
    val fieldHeight = computationalField.top - computationalField.bottom
    val sx = width.toFloat() / fieldWidth.toFloat()
    val sy = height.toFloat() / fieldHeight.toFloat()

    // Align the origin with the airfoil LE location and apply scale factors
    val originX = width / 4f
    val originY = height / 2f
    val matrix = Matrix().apply {
        setTranslate(originX, originY)
        // Flip along X axis since Canvas Y axis points downwards
        postScale(sx, -sy, originX, originY)
    }
    val streamlinesPlotBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val streamlinesPlotPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = DEFAULT_STROKE_COLOR
        strokeWidth = 2f
    }
    val airfoilPaint = Paint().apply {
        style = Paint.Style.FILL
        color = DEFAULT_FILL_COLOR
    }
    val streamlinesPlotCanvas = Canvas(streamlinesPlotBitmap)
    with(streamlinesPlotCanvas) {
        drawColor(DEFAULT_BACKGROUND_COLOR)
        val path = Path()
        forEach {
            path.reset()
            it.points.toPath(path, false)
            drawPath(path.apply { transform(matrix) }, streamlinesPlotPaint)
        }
        path.reset()
        drawPath(airfoilPoints.toPath(path).apply { transform(matrix) }, airfoilPaint)
    }
    return streamlinesPlotBitmap
}

/**
 * Generates a bitmap of the pressure distribution plot.
 *
 * @param airfoilPoints The points of the airfoil to use for the plot.
 * @param width The width of the bitmap. Defaults to `1080`.
 * @param height The height of the bitmap. Defaults to `1080`.
 * @return The bitmap of the pressure distribution plot.
 */
fun List<Point>.generatePressurePlotBitmap(
    airfoilPoints: List<Point>,
    width: Int = DEFAULT_PLOT_WIDTH,
    height: Int = DEFAULT_PLOT_HEIGHT
): Bitmap {
    val plotWidth = maxOf { it.x } - minOf { it.x }
    val plotHeight = maxOf { it.y } - minOf { it.y }
    val sx = width.toFloat() / plotWidth.toFloat()
    val sy = height.toFloat() / plotHeight.toFloat()
    val airfoilWidth = airfoilPoints.maxOf { it.x } - airfoilPoints.minOf { it.x }
    val airfoilS = width.toFloat() / airfoilWidth.toFloat()

    // Align the origin with the airfoil LE location and apply scale factors
    val originX = 0f
    val originY = height / 2f
    val matrix = Matrix().apply {
        setTranslate(originX, originY)
        // Flip along X axis since Canvas Y axis points downwards
        postScale(sx, -sy, originX, originY)
    }
    val airfoilMatrix = Matrix().apply {
        setTranslate(originX, originY)
        // Flip along X axis since Canvas Y axis points downwards
        // Apply same scaling along X and Y directions to keep the airfoil aspect ratio
        postScale(airfoilS, -airfoilS, originX, originY)
    }
    val pressurePlotBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val pressurePlotPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = DEFAULT_STROKE_COLOR
        strokeWidth = 2f
    }
    val airfoilPaint = Paint().apply {
        style = Paint.Style.FILL
        color = DEFAULT_FILL_COLOR
    }
    // Copy airfoil path locally to avoid modifying original
    val pressurePlotCanvas = Canvas(pressurePlotBitmap)
    with(pressurePlotCanvas) {
        drawColor(DEFAULT_BACKGROUND_COLOR)
        val path = Path()
        drawPath(toPath(path).apply { transform(matrix) }, pressurePlotPaint)
        path.reset()
        drawPath(airfoilPoints.toPath(path).apply { transform(airfoilMatrix) }, airfoilPaint)
    }
    return pressurePlotBitmap
}