package com.example.uniphoto.ui.readyPhoto

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.example.uniphoto.BuildConfig
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import org.kodein.di.Kodein
import java.io.File
import java.io.FileOutputStream

class ReadyPhotoViewModel(kodein: Kodein): KodeinViewModel(kodein) {

    lateinit var file: File

    val setVideoPreviewCommand = LiveArgEvent<RequestBuilder<Drawable>>()
    val shareIntentCommand = LiveArgEvent<Intent>()

    fun init(fileName: String?, context: Context) {
        fileName?.let {
            file = File("${context.filesDir}/UniPhoto/$fileName")
            setVideoPreviewCommand(
                Glide.with(context)
                    .load(Uri.fromFile(file)).thumbnail(0.9f))
        }
    }

    fun onSavedClicked() {
        val file = File(Environment.getExternalStoragePublicDirectory("UniPhoto"), file.name)

        if (!file.exists()) {
            file.mkdirs()
        }

        val fos = FileOutputStream(file)
        fos.flush()
        fos.close()
    }

    fun onShareClicked(context: Context) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "video/mp4"
        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",file))
        shareIntentCommand(intent)
    }
}