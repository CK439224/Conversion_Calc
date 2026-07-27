package com.numconversion.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.numconversion.app.R
import com.numconversion.app.ui.theme.calculatorColors

/** A result line with a trailing copy-to-clipboard action, shared by the calculator and converter. */
@Composable
fun AnswerDisplay(
    text: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    style: TextStyle = MaterialTheme.typography.displayMedium,
    onCopied: () -> Unit = {},
    testTag: String? = null
) {
    val clipboardManager = LocalClipboardManager.current
    val copyDescription = stringResource(R.string.cd_copy_result)
    val displayColors = MaterialTheme.calculatorColors
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = style,
            color = if (isError) MaterialTheme.colorScheme.error else displayColors.displayContent,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f).let { if (testTag != null) it.testTag(testTag) else it }
        )
        IconButton(
            onClick = {
                clipboardManager.setText(AnnotatedString(text))
                onCopied()
            },
            enabled = text.isNotBlank() && !isError,
            modifier = Modifier.heightIn(min = 48.dp).widthIn(min = 48.dp)
        ) {
            Icon(Icons.Filled.ContentCopy, contentDescription = copyDescription, tint = displayColors.displayIcon)
        }
    }
}
