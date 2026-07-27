package com.numconversion.app.ui.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class AppMode { CALCULATOR, CONVERT, HISTORY }

@Composable
fun ModeToggle(
    selected: AppMode,
    onSelect: (AppMode) -> Unit,
    tabs: List<Pair<AppMode, String>>,
    modifier: Modifier = Modifier
) {
    val selectedIndex = tabs.indexOfFirst { it.first == selected }.coerceAtLeast(0)
    TabRow(selectedTabIndex = selectedIndex, modifier = modifier) {
        tabs.forEach { (mode, label) ->
            Tab(
                selected = selected == mode,
                onClick = { onSelect(mode) },
                text = { Text(label) }
            )
        }
    }
}
