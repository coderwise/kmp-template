package com.example.myapp.app.common.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

private const val TRANSITION_DURATION = 700

internal fun popTransition() = NavDisplay.popTransitionSpec {
    slideInPopTransform()
} + NavDisplay.predictivePopTransitionSpec { _ ->
    slideInPopTransform()
}

internal fun slideInPopTransform() =
    (slideInHorizontally(
        initialOffsetX = { -it / 4 },
        animationSpec = tween(TRANSITION_DURATION),
    ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))) togetherWith
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(TRANSITION_DURATION),
        )
