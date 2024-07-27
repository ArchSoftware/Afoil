package com.archsoftware.afoil.core.designsystem.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset

/**
 * Shared axis full transition.
 */
fun <T> AnimatedContentTransitionScope<T>.sharedAxis(
    towards: AnimatedContentTransitionScope.SlideDirection,
    duration: Int = 250
): ContentTransform {
    return sharedAxisEnter(
        towards = towards,
        duration = duration
    ) togetherWith sharedAxisExit(
        towards = towards,
        duration = duration / 2
    )
}

/**
 * Shared axis enter transition.
 */
fun <T> AnimatedContentTransitionScope<T>.sharedAxisEnter(
    towards: AnimatedContentTransitionScope.SlideDirection,
    duration: Int = 250
): EnterTransition {
    val slideIntoAnimationSpec = tween<IntOffset>(durationMillis = duration)
    val fadeInAnimationSpec = tween<Float>(durationMillis = duration)
    val slideOffset = 100
    return slideIntoContainer(
        towards = towards,
        animationSpec = slideIntoAnimationSpec,
        initialOffset = { slideOffset * towards.offsetSign() }
    ) + fadeIn(
        animationSpec = fadeInAnimationSpec
    )
}

/**
 * Shared axis exit transition.
 */
fun <T> AnimatedContentTransitionScope<T>.sharedAxisExit(
    towards: AnimatedContentTransitionScope.SlideDirection,
    duration: Int = 250
): ExitTransition {
    val slideOutAnimationSpec = tween<IntOffset>(durationMillis = duration)
    val fadeOutAnimationSpec = tween<Float>(durationMillis = duration)
    val slideOffset = 100
    return slideOutOfContainer(
        animationSpec = slideOutAnimationSpec,
        towards = towards,
        targetOffset = { -slideOffset * towards.offsetSign() }
    ) + fadeOut(
        animationSpec = fadeOutAnimationSpec
    )
}

private fun AnimatedContentTransitionScope.SlideDirection.offsetSign(): Int =
    when(this) {
        AnimatedContentTransitionScope.SlideDirection.Left,
        AnimatedContentTransitionScope.SlideDirection.Start -> 1
        AnimatedContentTransitionScope.SlideDirection.Right,
        AnimatedContentTransitionScope.SlideDirection.End -> -1
        AnimatedContentTransitionScope.SlideDirection.Up -> 1
        else -> -1
    }
