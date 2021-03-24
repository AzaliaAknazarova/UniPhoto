package com.example.uniphoto.ui.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveEvent
import com.example.uniphoto.base.utils.Utils
import com.example.uniphoto.model.dataClasses.UserData
import com.example.uniphoto.model.repository.AuthorizationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.net.SocketTimeoutException

class LoginViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    private val authorizationRepository by instance<AuthorizationRepository>()

    val progressBarVisible = MutableLiveData(false)

    val launchTrialPeriodScreenCommand = LiveEvent()

    fun singIn(userName: String, password: String) {
        progressBarVisible.value = true
        launch {
            try {
                val token = authorizationRepository.signIn(
                    UserData(
                        username = userName,
                        password = password
                    )
                ).token
                withContext(Dispatchers.Main) {
                    saveAuthorizationToken(token)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    val message = when (exception) {
                        is SocketTimeoutException -> context.getString(R.string.error_timeout)
                        else -> exception.message
                    }
                    exception.printStackTrace()
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    progressBarVisible.value = false
                }
            }
        }
    }

    fun signUp(userName: String, email: String, password: String) {
        progressBarVisible.value = true
        launch {
            try {
                authorizationRepository.signUp(
                    UserData(
                        email = email,
                        username = userName,
                        password = password
                    )
                )
                withContext(Dispatchers.Main) {
                    singIn(userName, password)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    val message = when (exception) {
                        is SocketTimeoutException -> context.getString(R.string.error_timeout)
                        else -> exception.message
                    }
                    exception.printStackTrace()
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
        Utils.putTokenInSharedPref(token)
        launchTrialPeriodScreenCommand.call()
    }
}