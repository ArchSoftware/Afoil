package com.archsoftware.afoil.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.archsoftware.afoil.core.model.ComputationLog

/**
 * A composable that displays a [ComputationLog].
 *
 * @param log The log to display.
 * @param modifier The [Modifier] to apply to this row.
 */
@Composable
fun ComputationLogRow(
    log: ComputationLog,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = log.timestamp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = log.tag,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color(log.level.color))
                .size(32.dp)
        ) {
            Text(text = log.level.identifier)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = log.message,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color(log.level.color).takeIf {
                log.level == ComputationLog.Level.WARNING ||
                        log.level == ComputationLog.Level.ERROR
            } ?: Color.Unspecified
        )
    }
}

@Preview
@Composable
private fun ComputationLogRowPreview() {
    ComputationLogRow(
        log = ComputationLog(
            timestamp = "2023-01-01 12:00:00",
            tag = "ComputationLog",
            message = "This is a computation log message.",
            level = ComputationLog.Level.WARNING
        )
    )
}