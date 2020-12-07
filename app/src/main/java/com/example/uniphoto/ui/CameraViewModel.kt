package com.example.uniphoto.ui

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.base.kodein.KodeinViewModel
import org.kodein.di.Kodein
import java.io.Serializable

class CameraViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    val masksItemsList = MutableLiveData<List<MaskListItem>>()

    private val masksList = listOf (
        Pair(1, "sunglasses.sfb"),
        Pair(2, "yellow_sunglasses.sfb"),
        Pair(3, "hypno_glasses.sfb"),
        Pair(4, "hypno_glasses.sfb")
    )

    fun init() {
        masksItemsList.value = masksList.map {
            MaskListItem(
                id = it.first,
                mask = it.second
            )
        }
        Log.d("tag", "on init ${masksItemsList.value}")
    }

    data class MaskListItem(
        var id: Int,
        val imageView: Drawable? = null,
        val mask: String = ""
    ) : Serializable
}