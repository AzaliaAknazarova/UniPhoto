package com.example.uniphoto.ui.camera

import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import com.example.uniphoto.base.lifecycle.LiveEvent
import org.kodein.di.Kodein
import java.io.File
import java.io.Serializable

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

    var videoFile = File("")
    val mode = RecordType.Video

    private val masksList = listOf (
        Pair(1, "sunglasses.sfb"),
        Pair(2, "yellow_sunglasses.sfb"),
        Pair(3, "hypno_glasses.sfb"),
        Pair(4, "red_mask.sfb"),
        Pair(5, "blue_sunglasses.sfb")
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

    fun cameraButtonClicked() {
        when (mode) {
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
        declineCommand.call()
    }

    fun recordCompleted(file: File) {
        videoFile = file

        cameraFrameVisible.value = false
        acceptLayoutVisible.value = true
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