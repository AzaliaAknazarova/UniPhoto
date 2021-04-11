package com.example.uniphoto.ui.login

import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import com.example.uniphoto.base.lifecycle.LiveEvent
import org.kodein.di.Kodein

class SignInViewModel(kodein: Kodein): KodeinViewModel(kodein) {

    val userNameText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()

    val setUserNameError = LiveEvent()
    val setPasswordError = LiveEvent()
    val signInCommand = LiveArgEvent<Pair<String, String>>()

    fun onSignInButtonClicked() {
        val toSignIn = verify(userNameText.value, passwordText.value)
        if (toSignIn)
            signInCommand(Pair(userNameText.value!!, passwordText.value!!))
    }

    fun verify(userName: String?, password: String?) : Boolean {
        if (!isUserNameValid(userName)) {
            setUserNameError.set()
            return false
        }

        if (!isPasswordValid(password)) {
            setPasswordError.set()
            return false
        }

        return true
    }

    fun isUserNameValid(userName: String?): Boolean = !userName.isNullOrEmpty()
    fun isPasswordValid(password: String?): Boolean = !password.isNullOrEmpty()

}