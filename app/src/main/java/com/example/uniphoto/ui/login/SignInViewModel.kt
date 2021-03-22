package com.example.uniphoto.ui.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveEvent
import com.example.uniphoto.model.dataClasses.UserData
import com.example.uniphoto.model.repository.AuthorizationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.regex.Pattern

class SignInViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    private val authorizationRepository by instance<AuthorizationRepository>()

    val progressBarVisible = MutableLiveData(false)

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
        progressBarVisible.value = true
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
                withContext(Dispatchers.Main) {
                    val message = when (exception) {
                        is SocketTimeoutException -> context.getString(R.string.error_timeout)
                        else -> context.getString(R.string.error_unknown)
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    progressBarVisible.value = false
                }
            }
        }
    }

    fun saveAuthorizationToken(token: String) {
        Log.d("tag", "on saveAuthorizationToken $token")
    }
}