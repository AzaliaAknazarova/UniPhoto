package com.example.uniphoto.ui

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import org.kodein.di.Kodein
import java.io.Serializable

class CameraViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    val masksItemsList = MutableLiveData<List<MaskListItem>>()
    val masksItemsRecyclerVisible = MutableLiveData<Boolean>(false)
    val maskSelectedCommand = LiveArgEvent<Int>()

    private val masksList = listOf (
        Pair(1, "sunglasses.sfb"),
        Pair(2, "yellow_sunglasses.sfb"),
        Pair(3, "hypno_glasses.sfb"),
        Pair(4, "hypno_glasses.sfb")
    )

    fun init() {
        masksItemsList.value = masksList.map { pair ->
            MaskListItem(
                id = pair.first,
                mask = pair.second,
                onItemClicked = {maskSelectedCommand(pair.first)}
            )
        }
        Log.d("tag", "on init ${masksItemsList.value}")
    }

    data class MaskListItem(
        var id: Int,
        val imageView: Drawable? = null,
        val mask: String = "",
        val onItemClicked: ((Int) -> Unit)? = null
    ) : Serializable
}