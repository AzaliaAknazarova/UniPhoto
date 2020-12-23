package com.example.uniphoto.ui.photoView

import android.content.Context
import android.net.Uri
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import org.kodein.di.Kodein
import java.io.File

class PhotoViewViewModel(kodein: Kodein): KodeinViewModel(kodein) {

    val setupVideoControllerCommand = LiveArgEvent<Uri>()

    fun init(name: String?, context: Context) {
        name?.let {
            setupVideoControllerCommand(Uri.fromFile(File("${context.filesDir}/UniPhoto/$name")))
        }
    }
}