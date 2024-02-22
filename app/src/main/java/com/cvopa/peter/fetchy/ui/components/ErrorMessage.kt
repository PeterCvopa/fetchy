package com.cvopa.peter.fetchy.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.cvopa.peter.fetchy.R

@Composable
internal fun ErrorMessage(error: ErrorState?) {
    AnimatedVisibility(
        visible = error != null,
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(100))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row {
                Icon(
                    tint = Color.Red,
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = null
                )
            }

            Row {
                val errorText = error?.messageRes?.let {
                    stringResource(id = it)
                } ?: ""

                Text(
                    maxLines = 2,
                    color = Color.Red,
                    text = errorText
                )
            }
        }
    }
}


sealed class ErrorState(open val throwable: Throwable, val messageRes: Int) {
    data object NoInternet : ErrorState(IllegalStateException("No internet connection"), R.string.error_no_internet)
    data object InputErrorState : ErrorState(IllegalStateException("Input error "), R.string.error_input)
    data object AuthorizationErrorState : ErrorState(IllegalStateException("Wrong username or password"), R.string.authorization_input)
    data class General(override val throwable: Throwable) : ErrorState(throwable, R.string.error_general)
}
