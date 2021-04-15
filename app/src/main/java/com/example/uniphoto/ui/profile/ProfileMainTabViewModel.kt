package com.example.uniphoto.ui.profile

import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import com.example.uniphoto.base.lifecycle.LiveEvent
import com.example.uniphoto.base.utils.Utils
import com.example.uniphoto.model.repository.ContentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.lang.Exception

class ProfileMainTabViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    private val contentRepository by instance<ContentRepository>()

    val emailText = MutableLiveData<String>()
    val userNameText = MutableLiveData<String>()

    val launchGalleryCommand = LiveEvent()

    val launchLoginScreenCommand = LiveEvent()

    fun init() {
        getUserData()
    }

    private fun getUserData() {
        launch {
            try {
                val userData = contentRepository.getUserData(Utils.getTokenFromSharedPref())
                withContext(Dispatchers.Main) {
                    emailText.value = userData.email
                    userNameText.value = userData.username
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    fun onToGalleryLayoutClicked() {
        launchGalleryCommand.call()
    }

    fun onLogOutButtonClicked() {
        launchLoginScreenCommand.set()
    }

}