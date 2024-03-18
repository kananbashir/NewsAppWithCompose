package com.example.composenewscatcher.ui.util

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun GrayIndicatorBox(
    modifier: Modifier = Modifier,
    boxAlpha: Float = 1f,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = boxAlpha))
    ) {
        content()
    }
}

fun slideInWithFadeIn(): EnterTransition {
    return slideIn(
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        )
    ) { fullSize -> IntOffset(0, -fullSize.height * 2) } + fadeIn(
        animationSpec = tween(
            durationMillis = 100,
            easing = LinearOutSlowInEasing
        )
    )
}

fun slideOutWithFadeOut(): ExitTransition {
    return slideOut(
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    ) { fullSize -> IntOffset(0, -fullSize.height * 2) } + fadeOut(
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )
}