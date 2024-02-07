package com.cvopa.peter.play.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun LoadingIndicator(isLoading: Boolean) {
    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))

        }
    }
}