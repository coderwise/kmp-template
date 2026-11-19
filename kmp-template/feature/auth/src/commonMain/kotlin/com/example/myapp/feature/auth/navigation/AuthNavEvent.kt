package com.example.myapp.feature.auth.navigation

import com.coderwise.libs.navigation.NavEvent

sealed interface AuthNavEvent : NavEvent {
    data object ToSignUp : AuthNavEvent
}
