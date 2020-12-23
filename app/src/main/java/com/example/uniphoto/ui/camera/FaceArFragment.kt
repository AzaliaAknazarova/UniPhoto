package com.example.uniphoto.ui.camera

import android.graphics.*
import android.graphics.ImageFormat
import android.media.CamcorderProfile
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.ar.core.*
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.AugmentedFaceNode
import java.io.File
import java.io.FileOutputStream
import java.util.*

class FaceArFragment : ArFragment(), MaskSelectedListener, ImageCaptureListener {
    private var faceRegionsRenderable: ModelRenderable? = null

    var faceNodeMap = HashMap<AugmentedFace, AugmentedFaceNode>()
    private var changeModel: Boolean = false
    private var glasses: ArrayList<ModelRenderable> = ArrayList()
    lateinit var faceArView : View
    var session : Session? = null
    var takePicture = false
    val videoRecorder  = VideoRecorder()

    override fun getSessionConfiguration(session: Session?): Config {
        val config = Config(session)
        config.augmentedFaceMode = Config.AugmentedFaceMode.MESH3D
        return config
    }

    override fun getSessionFeatures(): MutableSet<Session.Feature> {
        return EnumSet.of(Session.Feature.FRONT_CAMERA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val frameLayout = super.onCreateView(inflater, container, savedInstanceState) as? FrameLayout
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        return  frameLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMask()
        faceArView = view
    }

    private fun initRenderable() {
        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse(
                    "sunglasses.sfb"))
            .build()
            .thenAccept { modelRenderable ->
                glasses.add(modelRenderable)
                faceRegionsRenderable = modelRenderable
                modelRenderable.isShadowCaster = false
                modelRenderable.isShadowReceiver = false
            }
        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse(
                "yellow_sunglasses.sfb" ))
            .build()
            .thenAccept { modelRenderable ->
                glasses.add(modelRenderable)
                modelRenderable.isShadowCaster = false
                modelRenderable.isShadowReceiver = false
            }
        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse(
                    "hypno_glasses.sfb"))
            .build()
            .thenAccept { modelRenderable ->
                glasses.add(modelRenderable)
                modelRenderable.isShadowCaster = false
                modelRenderable.isShadowReceiver = false
            }
        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse(
                "red_mask.sfb"))
            .build()
            .thenAccept { modelRenderable ->
                glasses.add(modelRenderable)
                modelRenderable.isShadowCaster = false
                modelRenderable.isShadowReceiver = false
            }
        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse(
                "blue_sunglasses.sfb"))
            .build()
            .thenAccept { modelRenderable ->
                glasses.add(modelRenderable)
                modelRenderable.isShadowCaster = false
                modelRenderable.isShadowReceiver = false
            }
    }

    private fun setMask() {
        initRenderable()

        val sceneView = this.arSceneView
        sceneView?.cameraStreamRenderPriority = Renderable.RENDER_PRIORITY_FIRST
        val scene = sceneView?.scene
        faceArView = sceneView

        scene?.addOnUpdateListener {
            Log.d("tag", "on scene?.addOnUpdateListener ${faceRegionsRenderable != null}")
            if (faceRegionsRenderable != null) {
                sceneView.session
                    ?.getAllTrackables(AugmentedFace::class.java)?.let {
                        for (f in it) {
                            if (!faceNodeMap.containsKey(f)) {
                                val faceNode = AugmentedFaceNode(f)
                                faceNode.setParent(scene)
                                faceNode.faceRegionsRenderable = faceRegionsRenderable
                                faceNodeMap.put(f, faceNode)
                            } else if (changeModel) {
                                faceNodeMap.getValue(f).faceRegionsRenderable = faceRegionsRenderable
                            }
                        }
                        // Remove any AugmentedFaceNodes associated with an AugmentedFace that stopped tracking.
                        val iter = faceNodeMap.entries.iterator()
                        while (iter.hasNext()) {
                            val entry = iter.next()
                            val face = entry.key
                            if (face.trackingState == TrackingState.STOPPED) {
                                val faceNode = entry.value
                                faceNode.setParent(null)
                                iter.remove()
                            }
                        }

                    }
            }
        }
    }

    fun saveImage(image: Image) {
        val yuv = YuvImage(ImageConverter.YUV420toNV21(image), ImageFormat.NV21, image.width, image.height, null)
        val path =
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/unipic_${System.currentTimeMillis()}.jpg"
        Log.d("tag", "on initViews $path")
        val stream = FileOutputStream(path)
        yuv.compressToJpeg(Rect(0, 0, image.width, image.height), 100, stream)
        stream.close()
    }

    private fun captureScreen(view: View){
        Log.d("tag","on captureScreen")
        val bitmap = Bitmap.createBitmap (view.width, view.height, Bitmap.Config.ARGB_8888);
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT)
        view.draw(canvas)
        val fos = FileOutputStream(
            File(
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "unipic_${System.currentTimeMillis()}.jpg"
            )
        )
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        Toast.makeText(requireContext(), "Screen captured", LENGTH_SHORT).show()
    }

    interface Listener {
        fun recordCompleted(fileName: String)
    }

    override fun maskSelected(maskId: Int) {
        Log.d("tag", "on maskSelected $maskId $faceRegionsRenderable $glasses")
        changeModel = true
        faceRegionsRenderable = glasses.get(maskId - 1)
    }

    override fun takePhotoClicked() {
        captureScreen(arSceneView)
    }

    override fun startVideoClicked() {
        videoRecorder.setSceneView(arSceneView)
        val orientation = resources.configuration.orientation
        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_HIGH, orientation)

        videoRecorder.onToggleRecord()

        Toast.makeText(requireContext(), "Recording is START", LENGTH_SHORT).show()
    }

    override fun stopVideoClicked() {
        videoRecorder.onToggleRecord()
        Toast.makeText(requireContext(), "Recording is STOP", LENGTH_SHORT).show()
        (parentFragment as CameraFragment).recordCompleted("")
        childFragmentManager.popBackStack()
    }
}
