package com.example.myapp.feature.auth.ui.login

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.core.ui.components.AppButton
import com.example.myapp.core.ui.layouts.LabeledField
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.feature.auth.ui.components.AuthAdaptiveLayout
import myapp.feature.auth.generated.resources.Res
import myapp.feature.auth.generated.resources.auth_email_label
import myapp.feature.auth.generated.resources.auth_error_label
import myapp.feature.auth.generated.resources.auth_no_account
import myapp.feature.auth.generated.resources.auth_password_label
import myapp.feature.auth.generated.resources.auth_sign_in
import myapp.feature.auth.generated.resources.auth_sign_in_title
import myapp.feature.auth.generated.resources.auth_sign_up
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    LoginScreenContent(uiState = uiState, onEvent = viewModel::onEvent)
}

@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
) {
    AuthAdaptiveLayout(
        title = stringResource(Res.string.auth_sign_in_title)
    ) {
        LoginFormContent(uiState, onEvent)
    }
}

@Composable
private fun ColumnScope.LoginFormContent(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
) {
    LabeledField(
        label = stringResource(Res.string.auth_email_label),
        value = uiState.email,
        onValueChange = { onEvent(LoginUiEvent.EmailChanged(it)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    LabeledField(
        label = stringResource(Res.string.auth_password_label),
        value = uiState.password,
        onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
        singleLine = true,
        isPassword = true,
        modifier = Modifier.fillMaxWidth(),
    )
    if (uiState.error != null) {
        Text(
            text = stringResource(Res.string.auth_error_label, uiState.error),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
        )
    }
    AppButton(
        text = stringResource(Res.string.auth_sign_in),
        onClick = { onEvent(LoginUiEvent.Submit) },
        enabled = uiState.email.isNotBlank() && uiState.password.isNotBlank() && !uiState.isLoading,
        modifier = Modifier.fillMaxWidth(),
    )
    if (uiState.isBypassEnabled) {
        TextButton(
            onClick = { onEvent(LoginUiEvent.DebugBypass) },
            enabled = !uiState.isLoading,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text("⚠ Skip login (debug)")
        }
    }
    Spacer(modifier = Modifier.weight(1f))
    Row(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.auth_no_account),
            style = MaterialTheme.typography.bodyMedium,
        )
        TextButton(onClick = { onEvent(LoginUiEvent.NavigateToSignUp) }) {
            Text(stringResource(Res.string.auth_sign_up))
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    MyAppTheme {
        LoginScreenContent(uiState = LoginUiState(), onEvent = {})
    }
}

@Preview
@Composable
private fun LoginScreenErrorPreview() {
    MyAppTheme {
        LoginScreenContent(
            uiState = LoginUiState(
                email = "user@example.com",
                error = "Invalid credentials",
            ),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun LoginScreenDarkPreview() {
    MyAppTheme(darkTheme = true) {
        LoginScreenContent(uiState = LoginUiState(), onEvent = {})
    }
}

@Preview
@Composable
private fun LoginScreenBypassPreview() {
    MyAppTheme {
        LoginScreenContent(uiState = LoginUiState(isBypassEnabled = true), onEvent = {})
    }
}
