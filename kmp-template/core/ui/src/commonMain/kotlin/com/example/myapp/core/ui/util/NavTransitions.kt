package com.example.myapp.core.ui.util

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

fun verticalSlideTransition() = NavDisplay.transitionSpec {
    slideInVertically(initialOffsetY = { it }, animationSpec = tween(400)) togetherWith
        slideOutVertically(targetOffsetY = { -it / 4 }, animationSpec = tween(400))
} + NavDisplay.popTransitionSpec {
    slideInVertically(initialOffsetY = { -it / 4 }, animationSpec = tween(400)) togetherWith
        slideOutVertically(targetOffsetY = { it }, animationSpec = tween(400))
} + NavDisplay.predictivePopTransitionSpec { _ ->
    slideInVertically(initialOffsetY = { -it / 4 }, animationSpec = tween(400)) togetherWith
        slideOutVertically(targetOffsetY = { it }, animationSpec = tween(400))
}
