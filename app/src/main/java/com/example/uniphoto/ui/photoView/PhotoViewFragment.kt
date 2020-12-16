package com.example.uniphoto.ui.photoView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment

class PhotoViewFragment: KodeinFragment<PhotoViewViewModel>() {

    override val viewModel by viewModel(PhotoViewViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    fun initViews() {

    }
}