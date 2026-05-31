package com.example.myapp.core.ui.util

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

private const val TRANSITION_DURATION = 400

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
