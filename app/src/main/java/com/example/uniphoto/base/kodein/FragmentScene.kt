package com.example.uniphoto.base.kodein

import com.example.uniphoto.base.lifecycle.LiveArgEvent
import com.example.uniphoto.base.lifecycle.LiveEvent
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

interface FragmentScene {
    val self: Fragment

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

    fun LifecycleOwner.bindTextTwoWay(liveData: MutableLiveData<String>, editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                liveData.value = s?.toString() ?: ""
            }
        })

        liveData.observe(self.viewLifecycleOwner, Observer {
            if (editText.text.toString() == it) {
                return@Observer
            }

            val oldSelection = editText.selectionStart
            val newLength = it?.length ?: 0
            val oldLength = editText.text?.length ?: 0
            val diff = newLength - oldLength
            editText.setText(it)

            var newSelection = when (diff) {
                1, -1 -> oldSelection + diff
                else -> newLength
            }

            if (newSelection < 0) {
                newSelection = 0
            }

            editText.setSelection(newSelection)
        })
    }

}

