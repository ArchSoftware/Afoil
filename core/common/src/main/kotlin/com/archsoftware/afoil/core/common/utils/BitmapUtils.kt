package com.archsoftware.afoil.core.common.utils

import android.graphics.Bitmap

/**
 * Executes the given block function on this [Bitmap] and then recycle it.
 *
 * @param block The block function to execute on this [Bitmap].
 */
inline fun Bitmap.use(block: (bitmap: Bitmap) -> Unit) {
    block(this)
    this.recycle()
}