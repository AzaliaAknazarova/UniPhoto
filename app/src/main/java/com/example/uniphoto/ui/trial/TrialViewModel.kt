package com.example.uniphoto.ui.trial

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveEvent
import com.example.uniphoto.base.utils.Utils
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
    val navigateToMainButtonVisible = MutableLiveData(false)

    val titleText = MutableLiveData<String>()
    val subtitleText = MutableLiveData<String>()

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
                    if (trial.daysToEnd == 0) {
                        titleText.value = context.getString(R.string.trial_timeout_title)
                        subtitleText.value = context.getString(R.string.trial_time_out_subtitle)
                        navigateToMainButtonVisible.value = false
                    } else {
                        titleText.value = "${trial.daysToEnd} " + context.getString(R.string.trial_days)
                        subtitleText.value = context.getString(R.string.trial_time_left_subtitle)
                        navigateToMainButtonVisible.value = true
                    }
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    val message = when (exception) {
                        is SocketTimeoutException -> context.getString(R.string.error_timeout)
                        else -> context.getString(R.string.error_unknown)
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    titleText.value = context.getString(R.string.trial_timeout_title)
                    subtitleText.value = context.getString(R.string.trial_time_out_subtitle)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    progressBarVisible.value = false
                }
            }
        }
    }

    fun onToMainButtonClicked() {
            launchMainScreenCommand.call()
    }

    fun onToLoginButtonClicked() {
        Utils.clearSharedPreferences()
        launchLoginScreenCommand.call()
    }

}