package com.example.uniphoto.ui.trial

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

class TrialViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    private val authorizationRepository by instance<AuthorizationRepository>()

    val progressBarVisible = MutableLiveData(false)

    val showTrialPeriodEndLayoutCommand = LiveEvent()
    val showTimeLeftLayoutCommand = LiveEvent()
    val launchLoginScreenCommand = LiveEvent()
    val launchMainScreenCommand = LiveEvent()

    fun init() {
        if (isUserSignIn()) {
            checkTrialPeriod()
        } else {
            launchLoginScreenCommand.call()
        }
    }

    fun isUserSignIn() : Boolean {
        val token = Utils.getTokenFromSharedPref()
        return token.isNotEmpty()
    }

    fun checkTrialPeriod() {
        progressBarVisible.value = true
        launch {
            try {
                val trial = authorizationRepository.checkTrial()
                withContext(Dispatchers.Main) {
                    if (trial.timeIsOut) {
                        showTrialPeriodEndLayoutCommand.call()
                    } else {
                        showTimeLeftLayoutCommand.call()
                    }
                }
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