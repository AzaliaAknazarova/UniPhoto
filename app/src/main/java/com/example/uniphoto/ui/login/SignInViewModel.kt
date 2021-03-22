package com.example.uniphoto.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveEvent
import com.example.uniphoto.model.dataClasses.UserData
import com.example.uniphoto.model.repository.AuthorizationRepository
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.util.regex.Pattern

class SignInViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    private val authorizationRepository by instance<AuthorizationRepository>()

    val userNameText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()

    val setUserNameError = LiveEvent()
    val setPasswordError = LiveEvent()

    fun onSignInButtonClicked() {
        var toSignIn = true

        if (!isUserNameValid(userNameText.value)) {
            toSignIn = false
            setUserNameError.call()
        }

        if (!isPasswordValid(passwordText.value)) {
            toSignIn = false
            setPasswordError.call()
        }

        if (toSignIn)
            signIn()
    }

    fun isUserNameValid(userName: String?): Boolean = !userName.isNullOrEmpty()

    fun isPasswordValid(password: String?): Boolean = !password.isNullOrEmpty()

    private fun signIn() {
        launch {
            try {
                val token = authorizationRepository.signIn(
                        UserData(
                                username = userNameText.value ?: "",
                                password = passwordText.value ?: ""
                        )
                ).token
                saveAuthorizationToken(token)
            } catch (exception: Exception) {

            }
        }
    }

    fun saveAuthorizationToken(token: String) {
        Log.d("tag", "on saveAuthorizationToken $token")
    }
}