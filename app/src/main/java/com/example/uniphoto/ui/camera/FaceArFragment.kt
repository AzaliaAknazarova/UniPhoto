package com.example.uniphoto.ui.camera

import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.PixelCopy
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.ar.core.AugmentedFace
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.AugmentedFaceNode
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.*

class FaceArFragment : ArFragment(), MaskSelectedListener, ImageCaptureListener {
    private var faceRegionsRenderable: ModelRenderable? = null

    var faceNodeMap = HashMap<AugmentedFace, AugmentedFaceNode>()
    private var changeModel: Boolean = false
    private var glasses: ArrayList<ModelRenderable> = ArrayList()
    lateinit var faceArView : View
    var takePicture = false

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
                        if (takePicture) {
                            val currentFrame = sceneView.arFrame
                            val currentImage = currentFrame?.acquireCameraImage()
                            if ( currentImage?.format == ImageFormat.YUV_420_888) {
                                Log.d("ImageFormat", "Image format is YUV_420_888")
                                YUV_420_888toNV21(currentImage)
                            }
                            takePicture = false
                        }

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
                        changeModel = false
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

    private fun YUV_420_888toNV21(image: Image) {
        val nv21: ByteArray
        val yBuffer: ByteBuffer = image.getPlanes().get(0).getBuffer()
        val uBuffer: ByteBuffer = image.getPlanes().get(1).getBuffer()
        val vBuffer: ByteBuffer = image.getPlanes().get(2).getBuffer()
        val ySize: Int = yBuffer.remaining()
        val uSize: Int = uBuffer.remaining()
        val vSize: Int = vBuffer.remaining()
        nv21 = ByteArray(ySize + uSize + vSize)

        //U and V are swapped
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)
        saveImage(nv21)
    }

    fun saveImage(nv21: ByteArray) {
        val yuv = YuvImage(nv21, ImageFormat.NV21, arSceneView.width, arSceneView.height, null)
        val path =
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/pics23567.jpg"
        Log.d("tag", "on initViews $path")
        val stream = FileOutputStream(path)
        yuv.compressToJpeg(Rect(0, 0, arSceneView.width, arSceneView.height), 100, stream)
        stream.close()
    }

    override fun maskSelected(maskId: Int) {
        Log.d("tag", "on maskSelected $maskId $faceRegionsRenderable $glasses")
        changeModel = true
        faceRegionsRenderable = glasses.get(maskId - 1)
    }

    override fun takePhotoClicked() {
        takePicture = true
    }
}