package com.example.myapp.desktop.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapp.app.common.Res
import com.example.myapp.app.common.desktop_about_app_name
import com.example.myapp.app.common.desktop_about_copyright
import com.example.myapp.app.common.desktop_about_description
import com.example.myapp.app.common.desktop_about_version
import org.jetbrains.compose.resources.stringResource

@Composable
fun AboutDialogContent(iconPainter: Painter?, appVersion: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(min = 280.dp)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (iconPainter != null) {
            Icon(
                painter = iconPainter,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(Res.string.desktop_about_app_name),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(Res.string.desktop_about_version, appVersion),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.desktop_about_description),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(Res.string.desktop_about_copyright),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
