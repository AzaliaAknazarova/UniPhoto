package com.example.uniphoto.ui.camera

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import com.example.uniphoto.base.lifecycle.LiveEvent
import org.kodein.di.Kodein
import java.io.Serializable

class CameraViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    val masksItemsList = MutableLiveData<List<MaskListItem>>()
    val masksItemsRecyclerVisible = MutableLiveData<Boolean>(false)
    val recordingIsStart = MutableLiveData<Boolean>(false)

    val maskSelectedCommand = LiveArgEvent<Int>()
    val takePictureCommand = LiveEvent()
    val startRecordCommand = LiveEvent()
    val stopRecordCommand = LiveEvent()

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
                if (recordingIsStart.value == true) {
                    stopRecordCommand.call()
                    recordingIsStart.value = false
                } else {
                    startRecordCommand.call()
                    recordingIsStart.value = true
                }
            }
            RecordType.Photo -> {
                takePictureCommand.call()
            }
        }
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