package com.example.uniphoto.ui.camera

import android.Manifest
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.uniphoto.R
import com.example.uniphoto.base.extensions.isPermissionGranted
import com.example.uniphoto.base.kodein.KodeinFragment
import com.example.uniphoto.model.MaskItemsListAdapter
import com.example.uniphoto.ui.galery.GalleryFragment.Companion.galleryFragmentArg
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_camera.backpressedImageView
import kotlinx.android.synthetic.main.fragment_camera.videoView
import java.io.File


/**
 * Created by nigelhenshaw on 2018/01/23.
 */
class CameraFragment : KodeinFragment<CameraViewModel>(), FaceArFragment.Listener {
    companion object {
        private const val cameraPermissionRequestCode = 45
    }

    override val viewModel by viewModel(CameraViewModel::class.java)

    val maskItemsAdapter = MaskItemsListAdapter()
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

//    private val settingsAlertDialog: AlertDialog by lazy {
//        AlertDialog.Builder(requireContext(), androidx.camera.core.R.style.AlertDialogTheme)
//            .setTitle(androidx.camera.core.R.string.alert_dialog_title)
//            .setMessage(androidx.camera.core.R.string.scr_camera_msg_no_permissions)
//            .setPositiveButton(androidx.camera.core.R.string.scr_camera_lbl_settings) { _, _ ->
//                if (activity != null) {
//                    startActivityForResult(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                        data = Uri.fromParts("package", requireActivity().packageName, null)
//                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    }, cameraPermissionRequestCode)
//                    Handler().postDelayed({ isCameraPermissionActivityHandled = true }, 1000)
//                }
//            }.setNegativeButton(androidx.camera.core.R.string.scr_camera_lbl_cancel) { dialog, _ ->
//                dialog.dismiss()
//                dismissAllowingStateLoss()
//            }.create().apply { setCanceledOnTouchOutside(false) }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
        viewModel.init()

        if (isAllPermissionsGranted()) {
            setTextureFragment()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                cameraPermissionRequestCode
            )
        }
    }

    private fun initViews() {
        masksRecyclerView.adapter = maskItemsAdapter

        photoModeTextView.setOnClickListener { viewModel.onChangeModeClicked(CameraViewModel.RecordType.Photo) }
        videoModeTextView.setOnClickListener { viewModel.onChangeModeClicked(CameraViewModel.RecordType.Video) }

        takePhotoImageView.setOnClickListener {
            Log.d("tag", "on bindViewModel takePhotoImageView.setOnClickListener $child")
            viewModel.cameraButtonClicked()
        }
        recordImageView.setOnClickListener { viewModel.completeRecordButtonClicked() }
        bottomSheetActionImageView.setOnClickListener {
            viewModel.masksItemsRecyclerVisible.value =
                viewModel.masksItemsRecyclerVisible.value != true
        }

        acceptPhotoButton.setOnClickListener { viewModel.acceptClicked() }
        declinePhotoButton.setOnClickListener { viewModel.declineClicked() }

        backpressedImageView.setOnClickListener { findNavController().navigateUp() }
    }

    private fun bindViewModel() {
        with(viewModel) {
            bindVisible(masksItemsRecyclerVisible, masksRecyclerView)
            bindVisible(recordingIsStart, recordImageView)

            bind(mode) {
                when (it) {
                    CameraViewModel.RecordType.Video -> {
                        videoModeTextView.typeface = Typeface.DEFAULT_BOLD
                        photoModeTextView.typeface = Typeface.DEFAULT
                    }
                    CameraViewModel.RecordType.Photo -> {
                        videoModeTextView.typeface = Typeface.DEFAULT
                        photoModeTextView.typeface = Typeface.DEFAULT_BOLD
                    }
                }
            }
            bind(cameraFrameVisible) {
                if (recordingIsStart.value == true)
                    textureFragment.isVisible = true
                else
                    textureFragment.isVisible = it
                cameraRelativeLayout.isVisible = it
                masksRelativeLayout.isVisible = it
                changeModeLayout.isVisible = it
            }
            bind(acceptLayoutVisible) {
                when (mode.value) {
                    CameraViewModel.RecordType.Video -> videoView.isVisible = it
                    CameraViewModel.RecordType.Photo -> previewImageView.isVisible = it
                }
                acceptPhotoLayout.isVisible = it
            }
            bind(masksItemsList) {
                maskItemsAdapter.items = it
                maskItemsAdapter.notifyDataSetChanged()
            }

            bindCommand(maskSelectedCommand) {
                castChild<MaskSelectedListener>()?.maskSelected(it)
            }
            bindCommand(takePictureCommand) {
                castChild<ImageCaptureListener>()?.takePhotoClicked()
            }
            bindCommand(startRecordCommand) {
                castChild<ImageCaptureListener>()?.startVideoClicked()
            }
            bindCommand(stopRecordCommand) {
                castChild<ImageCaptureListener>()?.stopVideoClicked()
            }
            bindCommand(launchPhotoCompleteViewCommand) {
                val arg = Bundle().apply { putString(galleryFragmentArg, it) }
                navigate(R.id.action_cameraFragment_to_readyPhotoFragment, arg)
            }
            bindCommand(declineCommand) {
                setTextureFragment()
            }
            bindCommand(setVideoViewCommand) {
                videoView.setVideoURI(it)
                videoView.setMediaController(MediaController(requireContext()))
                videoView.requestFocus()
                videoView.start()
            }
            bindCommand(setPhotoViewCommand) {
                it.into(previewImageView)
            }
        }
    }

    private fun setTextureFragment() {
        val arFragment = FaceArFragment()
        childFragmentManager.beginTransaction().apply {
            add(R.id.textureFragment, arFragment)
            commit()
        }
    }

    private fun closeArFragment() {
        childFragmentManager.beginTransaction().apply {
            remove(childFragmentManager.fragments.first())
            commit()
        }
    }

    override fun recordCompleted(file: File) {
        viewModel.recordCompleted(file)
        closeArFragment()
    }

    override fun photoTaken(file: File) {
        viewModel.photoTaken(file, requireContext())
        closeArFragment()
    }

    private fun isAllPermissionsGranted(): Boolean =
        requireContext().isPermissionGranted(Manifest.permission.CAMERA) && requireContext().isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)

}