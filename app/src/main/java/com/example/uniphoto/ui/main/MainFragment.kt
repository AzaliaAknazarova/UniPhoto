package com.example.uniphoto.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
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
        toGalleryLayout.setOnClickListener {
            navigate(R.id.action_mainFragment_to_galleryFragment)
        }
        /*toOnboardingButton.setOnClickListener {
            navigate(R.id.action_mainFragment_to_onboardingFragment)
        }*/
    }
}