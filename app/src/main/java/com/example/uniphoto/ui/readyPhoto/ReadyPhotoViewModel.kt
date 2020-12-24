package com.example.uniphoto.ui.readyPhoto

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.example.uniphoto.BuildConfig
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import org.kodein.di.Kodein
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files

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

    fun onSavedClicked(context: Context) {
        val externalDirectory = File("${Environment.getExternalStorageDirectory()}/UniPhoto")
        if (!externalDirectory.exists()) {
            externalDirectory.mkdirs()
        }

        Log.d("log", "on onSavedClicked ${file.name}")
        val externalFile = File(externalDirectory, file.name.toString())

        val fos = FileOutputStream(externalFile)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fos.write(Files.readAllBytes(file.toPath()))
        }
        fos.flush()
        fos.close()

        Toast.makeText(context, "File saved", Toast.LENGTH_SHORT).show()
    }

    fun onShareClicked(context: Context) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "video/mp4"
        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",file))
        shareIntentCommand(intent)
    }
}