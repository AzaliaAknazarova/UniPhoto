package com.example.uniphoto.ui.main

import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import com.example.uniphoto.base.lifecycle.LiveEvent
import org.kodein.di.Kodein

class MainViewModel(kodein: Kodein): KodeinViewModel(kodein) {

    val switchScreen = LiveArgEvent<Int>()
    val launchGalleryScreen = LiveEvent()
    val launchReadyPhotoScreen = LiveArgEvent<String>()

    fun init() {
        switchScreen(MainFragment.MainTab.Feed.index)
    }

    fun onBottomNavigationClicked(index: Int) {
        switchScreen(index)
    }

    fun onCameraBackPressed() {
        switchScreen(MainFragment.MainTab.Feed.index)
    }

    fun onReadyPhotoOpen(arg: String) {
        launchReadyPhotoScreen(arg)
    }

    fun onGalleryClicked() {
        launchGalleryScreen.call()
    }

    fun onReadyPhotoClosed() {
        switchScreen(MainFragment.MainTab.Feed.index)
    }

}