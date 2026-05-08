package com.example.myapp.app.common.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

internal fun popTransition() = NavDisplay.popTransitionSpec {
    slideInPopTransform()
} + NavDisplay.predictivePopTransitionSpec { _ ->
    slideInPopTransform()
}

internal fun slideInPopTransform() =
    slideInHorizontally(
        initialOffsetX = { -it / 4 },
        animationSpec = tween(700)
    ) + fadeIn(animationSpec = tween(700)) togetherWith
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(700)
        )
