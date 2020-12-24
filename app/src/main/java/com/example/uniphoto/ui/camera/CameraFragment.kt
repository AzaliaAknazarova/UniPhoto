package com.example.uniphoto.ui.camera

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.uniphoto.R
import com.example.uniphoto.base.extensions.isPermissionGranted
import com.example.uniphoto.base.kodein.KodeinFragment
import com.example.uniphoto.model.MaskItemsListAdapter
import com.example.uniphoto.ui.galery.GalleryFragment.Companion.galleryFragmentArg
import kotlinx.android.synthetic.main.fragment_camera.*
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
            // Wait for the view to be properly laid out
//            cameraPreviewView.postDelayed({ startCamera(ImageCapture.FLASH_MODE_OFF) }, 500)
            setTextureFragment()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                cameraPermissionRequestCode
            )
        }
    }

    private fun initViews() {
        masksRecyclerView.adapter = maskItemsAdapter
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

            bind(cameraFrameVisible) {
                if (recordingIsStart.value == true)
                    textureFragment.isVisible = true
                else
                    textureFragment.isVisible = it
                cameraRelativeLayout.isVisible = it
                masksRelativeLayout.isVisible = it
            }
            bind(acceptLayoutVisible) {
                previewImageView.isVisible = it
                acceptPhotoLayout.isVisible = it
            }
            bind(masksItemsList) {
                Log.d("tag", "on bindViewModel $it")

                maskItemsAdapter.items = it
                maskItemsAdapter.notifyDataSetChanged()
            }

            bindCommand(maskSelectedCommand) {
                Log.d("tag", "on bindViewModel $child")
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
        }
    }

    private fun setTextureFragment() {
        val arFragment = FaceArFragment()
        childFragmentManager.beginTransaction().apply {
            add(R.id.textureFragment, arFragment)
            commit()
        }
    }

    override fun recordCompleted(file: File) {
        viewModel.recordCompleted(file)
    }

    private fun isAllPermissionsGranted(): Boolean =
        requireContext().isPermissionGranted(Manifest.permission.CAMERA) && requireContext().isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)

}