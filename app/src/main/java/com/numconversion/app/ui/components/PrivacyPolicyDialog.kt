package com.numconversion.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.numconversion.app.R

/** Read-only in-app copy of the same policy published at the public privacy-policy URL. */
@Composable
fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.extraLarge, tonalElevation = 6.dp) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .sizeIn(maxHeight = 600.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(stringResource(R.string.privacy_policy_title), style = MaterialTheme.typography.headlineSmall)
                Text(
                    stringResource(R.string.privacy_policy_updated),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )
                Text(stringResource(R.string.privacy_policy_intro), style = MaterialTheme.typography.bodyMedium)

                PolicySection(R.string.privacy_policy_heading_collection, R.string.privacy_policy_body_collection)

                PolicySection(R.string.privacy_policy_heading_stored, R.string.privacy_policy_body_stored)
                Text(
                    stringResource(R.string.privacy_policy_body_history),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    stringResource(R.string.privacy_policy_body_leaves),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                PolicySection(R.string.privacy_policy_heading_permissions, R.string.privacy_policy_body_permissions)
                PolicySection(R.string.privacy_policy_heading_children, R.string.privacy_policy_body_children)
                PolicySection(R.string.privacy_policy_heading_contact, R.string.privacy_policy_body_contact)

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_close)) }
                }
            }
        }
    }
}

@Composable
private fun PolicySection(@StringRes headingRes: Int, @StringRes bodyRes: Int) {
    Text(
        stringResource(headingRes),
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(top = 20.dp, bottom = 4.dp)
    )
    Text(stringResource(bodyRes), style = MaterialTheme.typography.bodyMedium)
}
