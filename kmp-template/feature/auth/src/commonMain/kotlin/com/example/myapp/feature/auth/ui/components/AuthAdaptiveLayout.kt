package com.example.myapp.feature.auth.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.myapp.core.ui.components.AppIconButton
import com.example.myapp.core.ui.layouts.AppTopBar
import com.example.myapp.core.ui.theme.spacing
import com.example.myapp.core.ui.util.windowInfo

@Composable
fun AuthAdaptiveLayout(
    title: String,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val info = windowInfo
    Scaffold(
        modifier = modifier,
        topBar = {
            if (!info.isLandscape) {
                if (onBackClick != null) {
                    AppTopBar(
                        title = title,
                        onBackClick = onBackClick
                    )
                } else {
                    AppTopBar(title = title)
                }
            }
        }
    ) { paddingValues ->
        if (info.isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(MaterialTheme.spacing.gutter),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.base),
                ) {
                    if (onBackClick != null) {
                        AppIconButton(
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            onClick = onBackClick,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                    )
                    if (onBackClick != null) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .imePadding(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.base),
                ) {
                    content()
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(MaterialTheme.spacing.gutter),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.base),
            ) {
                content()
            }
        }
    }
}
