package com.numconversion.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.numconversion.app.ui.theme.LocalHapticsEnabled
import com.numconversion.app.ui.theme.calculatorColors

/**
 * Shared keypad button: a true circle floating in its grid cell (Aurora x Teal style), not
 * stretched to fill it. 56dp comfortably clears the 48dp minimum touch target and is smaller
 * than the cell on every phone size we support, which is what creates the visible gap around it.
 */
@Composable
fun CalcButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.calculatorColors.keyDigitContainer,
    contentColor: Color = MaterialTheme.calculatorColors.keyDigitContent
) {
    val described = if (contentDescription != null) {
        modifier.semantics { this.contentDescription = contentDescription }
    } else {
        modifier
    }
    val haptic = LocalHapticFeedback.current
    val hapticsEnabled = LocalHapticsEnabled.current
    Box(modifier = described.fillMaxHeight(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                if (hapticsEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
            enabled = enabled,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.testTag(label).size(56.dp)
        ) {
            Text(label, style = MaterialTheme.typography.headlineSmall)
        }
    }
}
