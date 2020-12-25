package com.example.uniphoto.ui.photoView

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.navigation.fragment.findNavController
import com.example.uniphoto.R
import com.example.uniphoto.base.extensions.isPermissionGranted
import com.example.uniphoto.base.kodein.KodeinFragment
import com.example.uniphoto.ui.galery.GalleryFragment.Companion.galleryFragmentArg
import kotlinx.android.synthetic.main.fragment_photo_view.*
import kotlinx.android.synthetic.main.fragment_photo_view.backpressedImageView

class PhotoViewFragment : KodeinFragment<PhotoViewViewModel>() {
    companion object {
        private const val writePermissionRequestCode = 45
    }

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

    private fun initViews() {
        backpressedImageView.setOnClickListener {
            findNavController().navigateUp()
        }
        downloadImageView.setOnClickListener {
            if (isAllPermissionsGranted()) {
                viewModel.onSavedClicked(requireContext())
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    writePermissionRequestCode
                )
            }
        }
        shareImageView.setOnClickListener { viewModel.onShareClicked(requireContext()) }
        deleteImageView.setOnClickListener { viewModel.onDeleteClicked() }
    }

    private fun bindViewModel() {
        with(viewModel) {
            bindCommand(setupVideoControllerCommand) {
                videoView.setVideoURI(it)
                videoView.setMediaController(MediaController(requireContext()))
                videoView.requestFocus()
                videoView.start()
            }
            bindCommand(setPhotoViewCommand) {
                previewImageView.setImageBitmap(it)
            }
            bindCommand(shareIntentCommand) {
                startActivity(Intent.createChooser(it, "Share"))
            }
            bindCommand(closeCommand) {
                findNavController().navigateUp()
            }
        }
    }

    private fun isAllPermissionsGranted(): Boolean =
        requireContext().isPermissionGranted(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

}