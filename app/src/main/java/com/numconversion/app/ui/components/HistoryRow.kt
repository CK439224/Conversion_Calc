package com.numconversion.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.numconversion.app.R
import com.numconversion.app.domain.history.HistoryEntry
import com.numconversion.app.domain.history.HistoryEntryType

@Composable
fun HistoryRow(entry: HistoryEntry, modifier: Modifier = Modifier) {
    val clipboardManager = LocalClipboardManager.current
    val copyDescription = stringResource(R.string.cd_copy_result)
    val tagText = when (entry.type) {
        HistoryEntryType.CALCULATOR -> stringResource(R.string.history_tag_calculator)
        HistoryEntryType.CONVERTER -> stringResource(R.string.history_tag_converter)
    }

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tagText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = entry.summary,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = formatRelativeTime(entry.timestampMillis),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = { clipboardManager.setText(AnnotatedString(entry.resultText)) },
                modifier = Modifier.heightIn(min = 48.dp).widthIn(min = 48.dp)
            ) {
                Icon(Icons.Filled.ContentCopy, contentDescription = copyDescription)
            }
        }
    }
}

private fun formatRelativeTime(timestampMillis: Long): String {
    val elapsedSeconds = (System.currentTimeMillis() - timestampMillis) / 1000
    return when {
        elapsedSeconds < 5 -> "Just now"
        elapsedSeconds < 60 -> "${elapsedSeconds}s ago"
        elapsedSeconds < 3600 -> "${elapsedSeconds / 60}m ago"
        elapsedSeconds < 86400 -> "${elapsedSeconds / 3600}h ago"
        else -> "${elapsedSeconds / 86400}d ago"
    }
}
