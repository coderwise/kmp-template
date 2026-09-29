package com.example.myapp.core.ui.util

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

private const val TRANSITION_DURATION = 400

private const val POP_TRANSITION_DURATION = 700

fun verticalSlideTransition() = NavDisplay.transitionSpec {
    slideInVertically(initialOffsetY = { it }, animationSpec = tween(TRANSITION_DURATION)) togetherWith
        slideOutVertically(targetOffsetY = { -it / 4 }, animationSpec = tween(TRANSITION_DURATION))
} + NavDisplay.popTransitionSpec {
    slideInVertically(initialOffsetY = { -it / 4 }, animationSpec = tween(TRANSITION_DURATION)) togetherWith
        slideOutVertically(targetOffsetY = { it }, animationSpec = tween(TRANSITION_DURATION))
} + NavDisplay.predictivePopTransitionSpec { _ ->
    slideInVertically(initialOffsetY = { -it / 4 }, animationSpec = tween(TRANSITION_DURATION)) togetherWith
        slideOutVertically(targetOffsetY = { it }, animationSpec = tween(TRANSITION_DURATION))
}

/**
 * The back slide for a screen pushed over another: the one underneath eases in from a quarter of
 * the way off the left while the screen on top slides out to the right.
 *
 * No fade on the incoming side. Fading makes the screen translucent, which sends all of it through
 * an offscreen layer on every frame of the transition — enough to starve the render thread when
 * that screen is a busy map.
 */
fun popTransition() = NavDisplay.popTransitionSpec {
    slideInPopTransform()
} + NavDisplay.predictivePopTransitionSpec { _ ->
    slideInPopTransform()
}

fun slideInPopTransform() =
    slideInHorizontally(
        initialOffsetX = { -it / 4 },
        animationSpec = tween(POP_TRANSITION_DURATION),
    ) togetherWith
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(POP_TRANSITION_DURATION),
        )
