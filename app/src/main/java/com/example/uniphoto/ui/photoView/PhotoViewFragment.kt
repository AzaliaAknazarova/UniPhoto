package com.example.uniphoto.ui.photoView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import com.example.uniphoto.ui.galery.GalleryFragment.Companion.galleryFragmentArg
import kotlinx.android.synthetic.main.fragment_photo_view.*

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
        bindViewModel()
        viewModel.init(arguments?.getString(galleryFragmentArg), requireContext())
    }

    fun initViews() {
        downloadImageView.setOnClickListener {

        }
        shareImageView.setOnClickListener {

        }
        deleteImageView.setOnClickListener {

        }
    }

    fun bindViewModel() {
        with(viewModel) {
            bindCommand(setupVideoControllerCommand) {
                videoView.setVideoURI(it)
                videoView.setMediaController(MediaController(requireContext()))
                videoView.requestFocus()
                videoView.start()
            }
        }
    }
}