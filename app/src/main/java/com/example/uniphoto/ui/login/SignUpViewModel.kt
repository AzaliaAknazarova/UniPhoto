package com.example.uniphoto.ui.login

import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import com.example.uniphoto.base.lifecycle.LiveEvent
import org.kodein.di.Kodein
import java.util.regex.Pattern

class SignUpViewModel(kodein: Kodein): KodeinViewModel(kodein) {

    val userNameText = MutableLiveData<String>()
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()

    val setUserNameError = LiveEvent()
    val setEmailError = LiveEvent()
    val setPasswordError = LiveEvent()
    val signUpCommand = LiveArgEvent<Triple<String, String, String>>()

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
            signUpCommand(Triple(userNameText.value!!, emailText.value!!, passwordText.value!!))
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

}