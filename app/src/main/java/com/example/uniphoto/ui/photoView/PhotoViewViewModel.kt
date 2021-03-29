package com.example.uniphoto.ui.photoView

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.uniphoto.BuildConfig
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import com.example.uniphoto.base.lifecycle.LiveEvent
import com.example.uniphoto.base.utils.Utils
import org.kodein.di.Kodein
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files

class PhotoViewViewModel(kodein: Kodein): KodeinViewModel(kodein) {

    lateinit var file : File

    val setPhotoViewCommand = LiveArgEvent<Bitmap>()
    val setupVideoControllerCommand = LiveArgEvent<Uri>()
    val shareIntentCommand = LiveArgEvent<Intent>()
    val closeCommand = LiveEvent()

    fun init(name: String?, context: Context) {
        name?.let {
            file = File("${context.filesDir}/UniPhoto/$name")
            if (Utils.isImageType(file))
                setPhotoViewCommand(BitmapFactory.decodeFile("${context.filesDir}/UniPhoto/$name"))
            else
                setupVideoControllerCommand(Uri.fromFile(file))
        }
    }

    fun onSavedClicked(context: Context) {
        val externalDirectory = File("${Environment.getExternalStorageDirectory()}/UniPhoto")
        if (!externalDirectory.exists()) {
            externalDirectory.mkdirs()
        }

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
        intent.type = if (Utils.isImageType(file)) "image/jpg" else "video/mp4"
        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",file))
        shareIntentCommand(intent)
    }

    fun onDeleteClicked() {
        file.delete()
        closeCommand.call()
    }
}