package com.example.myapp.feature.auth.ui.signup

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.core.ui.components.AppButton
import com.example.myapp.core.ui.layouts.LabeledField
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.feature.auth.ui.components.AuthAdaptiveLayout
import myapp.feature.auth.generated.resources.Res
import myapp.feature.auth.generated.resources.auth_confirm_password_label
import myapp.feature.auth.generated.resources.auth_email_label
import myapp.feature.auth.generated.resources.auth_error_label
import myapp.feature.auth.generated.resources.auth_password_label
import myapp.feature.auth.generated.resources.auth_sign_up
import myapp.feature.auth.generated.resources.auth_sign_up_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    SignUpScreenContent(uiState = uiState, onEvent = viewModel::onEvent)
}

@Composable
fun SignUpScreenContent(
    uiState: SignUpUiState,
    onEvent: (SignUpUiEvent) -> Unit,
) {
    AuthAdaptiveLayout(
        title = stringResource(Res.string.auth_sign_up_title),
        onBackClick = { onEvent(SignUpUiEvent.NavigateBack) }
    ) {
        SignUpFormContent(uiState, onEvent)
    }
}

@Composable
private fun ColumnScope.SignUpFormContent(
    uiState: SignUpUiState,
    onEvent: (SignUpUiEvent) -> Unit,
) {
    LabeledField(
        label = stringResource(Res.string.auth_email_label),
        value = uiState.email,
        onValueChange = { onEvent(SignUpUiEvent.EmailChanged(it)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    LabeledField(
        label = stringResource(Res.string.auth_password_label),
        value = uiState.password,
        onValueChange = { onEvent(SignUpUiEvent.PasswordChanged(it)) },
        singleLine = true,
        isPassword = true,
        modifier = Modifier.fillMaxWidth(),
    )
    LabeledField(
        label = stringResource(Res.string.auth_confirm_password_label),
        value = uiState.confirmPassword,
        onValueChange = { onEvent(SignUpUiEvent.ConfirmPasswordChanged(it)) },
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
    Spacer(modifier = Modifier.weight(1f))
    AppButton(
        text = stringResource(Res.string.auth_sign_up),
        onClick = { onEvent(SignUpUiEvent.Submit) },
        enabled = uiState.email.isNotBlank() &&
            uiState.password.isNotBlank() &&
            uiState.confirmPassword.isNotBlank() &&
            !uiState.isLoading,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview
@Composable
private fun SignUpScreenPreview() {
    MyAppTheme {
        SignUpScreenContent(uiState = SignUpUiState(), onEvent = {})
    }
}

@Preview
@Composable
private fun SignUpScreenErrorPreview() {
    MyAppTheme {
        SignUpScreenContent(
            uiState = SignUpUiState(
                password = "pass1",
                confirmPassword = "pass2",
                error = "Passwords do not match",
            ),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun SignUpScreenDarkPreview() {
    MyAppTheme(darkTheme = true) {
        SignUpScreenContent(uiState = SignUpUiState(), onEvent = {})
    }
}
