package com.example.uniphoto.base.kodein

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.uniphoto.base.lifecycle.LiveArgEvent
import com.example.uniphoto.base.lifecycle.LiveEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.w3c.dom.Text


interface FragmentScene {
    fun <T> LifecycleOwner.bind(liveData: MutableLiveData<T>, action: (T) -> Unit) {
        liveData.observe(this, Observer { action(it) })
    }

    fun <T> LifecycleOwner.bindCommand(liveEvent: LiveArgEvent<T>, action: (T) -> Unit) {
        liveEvent.observe(this, Observer { action(it) })
    }

    fun LifecycleOwner.bindCommand(liveEvent: LiveEvent, action: () -> Unit) {
        liveEvent.observe(this, Observer { action() })
    }

    fun LifecycleOwner.bindText(liveData: MutableLiveData<String>, textView: TextView) {
        liveData.observe(this, Observer { textView.text = liveData.value })
    }

    fun LifecycleOwner.bindVisible(liveData: MutableLiveData<Boolean>, view: View, invisible: Boolean = false, action: ((Boolean) -> Unit)? = null) =
        liveData.observe(this, Observer {
            if (invisible) view.isInvisible = it
            else view.isVisible = it
            action?.invoke(it)
        })

    fun LifecycleOwner.bindCheckable(liveData: MutableLiveData<Boolean>, compoundButton: CompoundButton) {
        liveData.observe(this, Observer {
            if (compoundButton.isChecked != it) {
                compoundButton.isChecked = it
            }
        })
    }

}

