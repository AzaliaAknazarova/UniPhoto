package com.example.uniphoto.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment

/**
 * Created by nigelhenshaw on 2018/01/23.
 */
class PreviewFragment : KodeinFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TAG", "on onCreateView PreviewFragment ")
    }
}