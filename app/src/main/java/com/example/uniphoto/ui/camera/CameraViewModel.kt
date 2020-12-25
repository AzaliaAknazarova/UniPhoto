package com.example.uniphoto.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.RequestBuilder
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import com.example.uniphoto.base.lifecycle.LiveEvent
import org.kodein.di.Kodein
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.File
import java.io.FileOutputStream
import java.io.Serializable
import java.nio.file.Files

class CameraViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    val masksItemsList = MutableLiveData<List<MaskListItem>>()
    val masksItemsRecyclerVisible = MutableLiveData<Boolean>(false)
    val recordingIsStart = MutableLiveData<Boolean>(false)
    val cameraFrameVisible = MutableLiveData<Boolean>(true)
    val acceptLayoutVisible = MutableLiveData<Boolean>(false)

    val maskSelectedCommand = LiveArgEvent<Int>()
    val takePictureCommand = LiveEvent()
    val startRecordCommand = LiveEvent()
    val stopRecordCommand = LiveEvent()
    val launchPhotoCompleteViewCommand = LiveArgEvent<String>()
    val declineCommand = LiveEvent()
    val setVideoViewCommand = LiveArgEvent<Uri>()
    val setPhotoViewCommand = LiveArgEvent<Bitmap>()

    var videoFile = File("")
    var mode = MutableLiveData<RecordType>(RecordType.Photo)

    private val masksList = listOf (
        Pair(1, "sunglasses.sfb"),
        Pair(2, "yellow_sunglasses.sfb"),
        Pair(3, "hypno_glasses.sfb"),
        Pair(4, "wow.sfb"),
        Pair(5, "purple_cat.sfb"),
        Pair(6, "blue.sfb"),
        Pair(7, "black.sfb")
    )

    fun init() {
        masksItemsList.value = masksList.map { pair ->
            MaskListItem(
                id = pair.first,
                mask = pair.second,
                onItemClicked = { maskSelectedCommand(pair.first) }
            )
        }
        Log.d("tag", "on init ${masksItemsList.value}")
    }

    fun onChangeModeClicked(newMode: RecordType) {
        mode.value = newMode
    }

    fun cameraButtonClicked() {
        when (mode.value) {
            RecordType.Video -> {
                startRecordCommand.call()
                recordingIsStart.value = true
                cameraFrameVisible.value = false
            }
            RecordType.Photo -> {
                takePictureCommand.call()
            }
        }
    }

    fun completeRecordButtonClicked() {
        stopRecordCommand.call()
        recordingIsStart.value = false
    }

    fun acceptClicked() {
        launchPhotoCompleteViewCommand(videoFile.name)
        cameraFrameVisible.value = true
        acceptLayoutVisible.value = false
    }

    fun declineClicked() {
        cameraFrameVisible.value = true
        acceptLayoutVisible.value = false
        videoFile.delete()
        declineCommand.call()
    }

    fun recordCompleted(file: File) {
        videoFile = file

        cameraFrameVisible.value = false
        acceptLayoutVisible.value = true

        setVideoViewCommand(Uri.fromFile(videoFile))
    }

    fun photoTaken(file: File, context: Context) {
        cameraFrameVisible.value = false
        acceptLayoutVisible.value = true

        Log.d("tag","on photoTaken ${file.path}")
        val retriever = FFmpegMediaMetadataRetriever()
        retriever.setDataSource(file.path)

        val bitmap = retriever.frameAtTime

        val internalDirectory = File("${context.filesDir}/UniPhoto")
        if (!internalDirectory.exists()) {
            internalDirectory.mkdirs()
        }

        val externalFile = File(internalDirectory, "pic_" + System.currentTimeMillis() + ".jpg")

        val fos = FileOutputStream(externalFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()

        Toast.makeText(context, "Photo saved", Toast.LENGTH_SHORT).show()

        setPhotoViewCommand(bitmap)

        file.delete()
    }

    enum class RecordType {
        Video,
        Photo
    }

    data class MaskListItem(
        var id: Int,
        val imageView: Drawable? = null,
        val mask: String = "",
        val onItemClicked: ((Int) -> Unit)? = null
    ) : Serializable
}