package com.example.vividay.presentation.sign_in

data class SignInState(
    val isSignInSuccessful: Boolean = true,
    val signInError: String? = null
)
