package com.example.uniphoto.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.uniphoto.R
import com.example.uniphoto.base.extensions.isPermissionGranted
import com.example.uniphoto.base.kodein.KodeinFragment
import com.example.uniphoto.model.MaskItemsListAdapter
import kotlinx.android.synthetic.main.fragment_camera.*

/**
 * Created by nigelhenshaw on 2018/01/23.
 */
class CameraFragment : KodeinFragment<CameraViewModel>() {
    companion object {
        private const val cameraPermissionRequestCode = 45
        const val MIN_OPENGL_VERSION = 3.0
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

        if (isAllPermissionsGranted()) {
            // Wait for the view to be properly laid out
//            cameraPreviewView.postDelayed({ startCamera(ImageCapture.FLASH_MODE_OFF) }, 500)
            setTextureFragment()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), cameraPermissionRequestCode)
        }

        initViews()
        bindViewModel()
        viewModel.init()
    }

    private fun initViews() {
        masksRecyclerView.adapter = maskItemsAdapter
    }

    private fun bindViewModel() {
        bind(viewModel.masksItemsList) {
            Log.d("tag", "on bindViewModel $it")

            maskItemsAdapter.items = it
            maskItemsAdapter.notifyDataSetChanged()
        }
    }

    private fun setTextureFragment() {
        val arFragment = FaceArFragment()
        childFragmentManager.beginTransaction().apply {
            add(R.id.textureFragment, arFragment)
            commit()
        }

    }

    private fun startCamera(flashMode: Int, cameraMode: Int? = null) {
        if (imageCapture != null) {
            if (cameraMode == null) {
                imageCapture!!.flashMode = flashMode
                return
            } else
                camera = null
        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()

            imageCapture = ImageCapture.Builder()
                .setFlashMode(flashMode)
                .build()

            val cameraSelector =
                if (cameraMode != CameraSelector.LENS_FACING_BACK) {
//                    viewModel.isFrontCameraEnabled = true
                    CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build()
                } else
                    CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

            try {
                cameraProvider.unbindAll()
                if (camera == null) {
                    camera =
                        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                }
                preview.setSurfaceProvider(cameraPreviewView.surfaceProvider)
            } catch (exc: Exception) {

            } finally {
                Handler().postDelayed({
//                    viewModel.antiGlitchLayoutVisible.value = false
                }, 30)
            }

        }, ContextCompat.getMainExecutor(context))
    }

//    private fun takePhoto() {
//        val imageCapture = imageCapture ?: return
//        val file = createImageFile(requireContext())
//
//        if (file != null) {
//            val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
//
//            imageCapture.takePicture(
//                outputOptions,
//                ContextCompat.getMainExecutor(context),
//                object : ImageCapture.OnImageSavedCallback {
//                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
////                        MediaPlayer.create(requireContext(), androidx.camera.core.R.raw.camera_sound).start()
//
////                        viewModel.photoTaken(file) {
////                            //dirty temp hack
////                            if (completedPhotosList != null) {
////                                completedPhotosList.adapter!!.notifyItemInserted(viewModel.photoPaths.lastIndex)
////                            }
////                        }
//                    }
//
//                    override fun onError(exc: ImageCaptureException) {
//
//                    }
//                })
//        } else {
//
//        }
//    }

//    private fun showSettingsDialog() = settingsAlertDialog.show()
    private fun isAllPermissionsGranted(): Boolean =
        requireContext().isPermissionGranted(Manifest.permission.CAMERA)

}