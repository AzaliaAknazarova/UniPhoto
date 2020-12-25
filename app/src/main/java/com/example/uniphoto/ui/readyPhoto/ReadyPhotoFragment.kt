package com.example.uniphoto.ui.readyPhoto

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import com.example.uniphoto.ui.galery.GalleryFragment
import kotlinx.android.synthetic.main.fragment_ready_photo.*
import kotlinx.android.synthetic.main.item_video.view.*

class ReadyPhotoFragment: KodeinFragment<ReadyPhotoViewModel>() {

    override val viewModel by viewModel(ReadyPhotoViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ready_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
        viewModel.init(arguments?.getString(GalleryFragment.galleryFragmentArg), requireContext())
    }

    private fun initViews() {
        toMainFragmentTextView.setOnClickListener {
            navigate(R.id.action_readyPhotoFragment_to_mainFragment)
        }
        backpressedImageView.setOnClickListener {
            findNavController().navigateUp()
        }
        savePhotoButton.setOnClickListener { viewModel.onSavedClicked(requireContext()) }
        shareInSocialNetworks.setOnClickListener { viewModel.onShareClicked(requireContext()) }
    }

    private fun bindViewModel() {
        with(viewModel) {
            bindCommand(setVideoPreviewCommand) {
                it.into(previewImageView)
            }
            bindCommand(shareIntentCommand) {
                startActivity(Intent.createChooser(it, "Share"))
            }
        }
    }
}
