package com.example.uniphoto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import com.example.uniphoto.ui.camera.CameraViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment: KodeinFragment<MainViewModel>() {

    override val viewModel by viewModel(MainViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    fun initViews() {
        addNewPhotoButton.setOnClickListener {
            navigate(R.id.action_mainFragment_to_cameraFragment)
        }
    }
}