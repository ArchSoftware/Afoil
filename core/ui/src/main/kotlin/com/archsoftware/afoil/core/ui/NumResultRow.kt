package com.archsoftware.afoil.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.archsoftware.afoil.core.common.utils.format

@Composable
fun NumResultRow(
    name: String,
    value: Double,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value.format(4),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun NumResultRow(
    name: AnnotatedString,
    value: Double,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value.format(4),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun NumResultRowPreview() {
    NumResultRow(
        name = "Name",
        value = 0.0
    )
}