package com.example.uniphoto.ui.login

import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.model.dataClasses.UserData
import com.example.uniphoto.model.repository.AuthorizationRepository
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class LoginViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    private val authorizationRepository by instance<AuthorizationRepository>()

    fun onSignUpButtonClicked() {
        launch {
            authorizationRepository.signUp(
                UserData(
                    email = "ph@gmail.com",
                    username = "kkkkkkashka",
                    password = "12345678"
                )
            )
        }
    }
}