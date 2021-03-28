package com.example.uniphoto.ui.profile

import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import com.example.uniphoto.base.lifecycle.LiveEvent
import org.kodein.di.Kodein

class ProfileMainTabViewModel(kodein: Kodein): KodeinViewModel(kodein) {

    val launchGalleryCommand = LiveEvent()

    fun onToGalleryLayoutClicked() {

        launchGalleryCommand.call()
    }

}