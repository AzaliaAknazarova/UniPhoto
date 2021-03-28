package com.example.uniphoto.ui.main

import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import com.example.uniphoto.base.lifecycle.LiveEvent
import org.kodein.di.Kodein

class MainViewModel(kodein: Kodein): KodeinViewModel(kodein) {

    val switchScreen = LiveArgEvent<Int>()
    val closeCameraCommand = LiveEvent()
    val launchGalleryScreen = LiveEvent()
    val launchReadyPhotoScreen = LiveArgEvent<String>()

    fun onBottomNavigationClicked(index: Int) {
        switchScreen(index)
    }

    fun onCameraBackPressed() {
        closeCameraCommand.call()
    }

    fun onReadyPhotoOpen(arg: String) {
        launchReadyPhotoScreen(arg)
    }

    fun onGalleryClicked() {
        launchGalleryScreen.call()
    }

}