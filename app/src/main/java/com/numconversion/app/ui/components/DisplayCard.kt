package com.numconversion.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.numconversion.app.ui.theme.calculatorColors

/** The panel behind [AnswerDisplay] — a white/near-black card with a hairline teal border. */
@Composable
fun DisplayCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    val colors = MaterialTheme.calculatorColors
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(containerColor = colors.displayBackground),
        border = BorderStroke(1.dp, colors.displayBorder),
        content = content
    )
}
