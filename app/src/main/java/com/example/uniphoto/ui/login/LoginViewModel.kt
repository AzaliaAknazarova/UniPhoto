package com.example.uniphoto.ui.login

import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.model.dataClasses.UserData
import com.example.uniphoto.model.repository.AuthorizationRepository
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.util.regex.Pattern

class LoginViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    private val authorizationRepository by instance<AuthorizationRepository>()

    fun onSignUpButtonClicked() {

    }

    private fun signUp() {
        launch {
            try {

            } catch (exception: Exception) {

            }
        }
    }
}