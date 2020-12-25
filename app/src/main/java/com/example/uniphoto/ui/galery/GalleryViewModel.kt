package com.example.uniphoto.ui.galery

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import org.kodein.di.Kodein
import java.io.File
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class GalleryViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    val dateFormat =  SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    val videoItemsList = MutableLiveData<List<VideoListItem>>()
    val launchVideoViewCommand = LiveArgEvent<String>()

    fun init(context: Context) {

        loadAllFilesFromDirectory(context)
    }

    private fun loadAllFilesFromDirectory(context: Context) {
        val filesList = File("${context.filesDir}/UniPhoto").listFiles()
        Log.d("tag", "on loadAllFilesFromDirectory $filesList")
         filesList?.let {
            videoItemsList.value = it.map {file ->
                Log.d("tag", "on loadAllFilesFromDirectory ${file.name}")
                VideoListItem(
                    name = file.name,
                    file = file,
                    date = dateFormat.format(file.lastModified()).toString(),
                    imageView = Glide.with(context).load(Uri.fromFile(file)).thumbnail(0.1f),
                    onItemClicked = {position ->
                        launchVideoViewCommand(videoItemsList.value!![position].name)
                    }
                )
            }
        }
    }

    data class VideoListItem(
        val name: String,
        val file: File,
        val date: String,
        val imageView: RequestBuilder<Drawable>,
        val onItemClicked: ((Int) -> Unit)? = null
    ) : Serializable
}