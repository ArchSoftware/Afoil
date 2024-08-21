package com.archsoftware.afoil.core.designsystem.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Modify element to allow to free scroll when width and/or the height of the content is bigger
 * than max constraints allow.
 *
 * @param state state of the scroll
 * @param enabled whether or not scrolling via touch input is enabled
 * @param flingBehavior logic describing fling behavior when drag has finished with velocity.
 * If null, default from [ScrollableDefaults.flingBehavior] will be used
 * @see [rememberFreeScrollState]
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Modifier.freeScroll(
    state: FreeScrollState,
    enabled: Boolean = true,
    flingBehavior: FlingBehavior? = null,
): Modifier {
    val fling = flingBehavior ?: ScrollableDefaults.flingBehavior()
    val overscrollEffect = ScrollableDefaults.overscrollEffect()
    val coroutineScope = rememberCoroutineScope()
    val draggable2DState = rememberDraggable2DState { dragAmount ->
        performScroll(
            horizontalScrollState = state.horizontalScrollState,
            verticalScrollState = state.verticalScrollState,
            dragAmount = dragAmount,
            coroutineScope = coroutineScope
        )
    }

    return this then Modifier.horizontalScroll(
        state = state.horizontalScrollState,
        enabled = false
    ).verticalScroll(
        state = state.verticalScrollState,
        enabled = false
    ).overscroll(
        overscrollEffect = overscrollEffect
    ).draggable2D(
        state = draggable2DState,
        enabled = enabled,
        onDragStopped = { velocity ->
            performFling(
                horizontalScrollState = state.horizontalScrollState,
                verticalScrollState = state.verticalScrollState,
                velocity = velocity,
                flingBehavior = fling,
                overscrollEffect = overscrollEffect,
                coroutineScope = coroutineScope
            )

        }
    )
}

/**
 * Create and remember the [ScrollState] based on the currently appropriate scroll configuration
 * to allow changing scroll position or observing scroll behavior.
 *
 * @param initial initial scroller position to start with
 */
@Composable
fun rememberFreeScrollState(initial: IntOffset = IntOffset.Zero): FreeScrollState {
    val horizontalScrollState = rememberScrollState(initial.x)
    val verticalScrollState = rememberScrollState(initial.y)

    return rememberSaveable(initial, saver = FreeScrollState.Saver) {
        FreeScrollState(
            initial = initial,
            horizontalScrollState = horizontalScrollState,
            verticalScrollState = verticalScrollState
        )
    }
}

data class FreeScrollState(
    val initial: IntOffset = IntOffset.Zero,
    val horizontalScrollState: ScrollState,
    val verticalScrollState: ScrollState
) {
    companion object {
        val Saver: Saver<FreeScrollState, *> = mapSaver(
            save = {
                mapOf(
                    "initialX" to it.initial.x,
                    "initialY" to it.initial.y,
                    "horizontalScrollState" to it.horizontalScrollState.value,
                    "verticalScrollState" to it.verticalScrollState.value
                )
            },
            restore = {
                FreeScrollState(
                    initial = IntOffset(it["initialX"] as Int, it["initialY"] as Int),
                    horizontalScrollState = ScrollState(it["horizontalScrollState"] as Int),
                    verticalScrollState = ScrollState(it["verticalScrollState"] as Int)
                )
            }
        )
    }
}

private fun performScroll(
    horizontalScrollState: ScrollState,
    verticalScrollState: ScrollState,
    dragAmount: Offset,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        horizontalScrollState.scrollBy(-dragAmount.x)
    }
    coroutineScope.launch {
        verticalScrollState.scrollBy(-dragAmount.y)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun performFling(
    horizontalScrollState: ScrollState,
    verticalScrollState: ScrollState,
    velocity: Velocity,
    flingBehavior: FlingBehavior,
    overscrollEffect: OverscrollEffect,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        val performFling: suspend (Velocity) -> Velocity = { velocity ->
            var result: Velocity = velocity
            horizontalScrollState.scroll {
                with(flingBehavior) {
                    result = result.copy(x = -performFling(velocity.x), y = 0f)
                }
            }
            (velocity - result)
        }
        if ((horizontalScrollState.canScrollForward && -velocity.x > 0)
            || (horizontalScrollState.canScrollBackward && -velocity.x < 0)) {
            overscrollEffect.applyToFling(-velocity, performFling)
        } else {
            performFling(-velocity)
        }
    }
    coroutineScope.launch {
        val performFling: suspend (Velocity) -> Velocity = { velocity ->
            var result: Velocity = velocity
            verticalScrollState.scroll {
                with(flingBehavior) {
                    result = result.copy(x = 0f, y = -performFling(velocity.y))
                }
            }
            (velocity - result)
        }
        if ((verticalScrollState.canScrollForward && -velocity.y > 0)
            || (verticalScrollState.canScrollBackward && -velocity.y < 0)) {
            overscrollEffect.applyToFling(-velocity, performFling)
        } else {
            performFling(-velocity)
        }
    }
}