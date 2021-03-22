package com.example.uniphoto.ui.login

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
import java.net.SocketTimeoutException
import java.util.regex.Pattern

class SignUpViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    private val authorizationRepository by instance<AuthorizationRepository>()

    val progressBarVisible = MutableLiveData(false)

    val userNameText = MutableLiveData<String>()
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()

    val setUserNameError = LiveEvent()
    val setEmailError = LiveEvent()
    val setPasswordError = LiveEvent()

    fun onSignUpButtonClicked() {
        var toSignUp = true

        if (!isUserNameValid(userNameText.value)) {
            toSignUp = false
            setUserNameError.call()
        }

        if (!isEmailValid(emailText.value)) {
            toSignUp = false
            setEmailError.call()
        }

        if (!isPasswordValid(emailText.value)) {
            toSignUp = false
            setPasswordError.call()
        }

        if (toSignUp)
            signUp()
    }

    fun isUserNameValid(username: String?): Boolean = !username.isNullOrEmpty()

    fun isEmailValid(email: String?): Boolean {
        if (email.isNullOrEmpty())
            return false

        return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email as CharSequence).matches()

    }

    fun isPasswordValid(password: String?): Boolean {
        if (password.isNullOrEmpty())
            return false

        return Pattern.compile(
                "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+$).{4,}$"
        ).matcher(password as CharSequence).matches()
    }

    private fun signUp() {
        progressBarVisible.value = true
        launch {
            try {
                authorizationRepository.signUp(
                        UserData(
                                email = emailText.value ?: "",
                                username = userNameText.value ?: "",
                                password = passwordText.value ?: ""
                        )
                )
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
}