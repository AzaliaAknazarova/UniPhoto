package com.example.uniphoto.ui.camera

import android.media.CamcorderProfile
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.ar.core.*
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.AugmentedFaceNode
import java.io.File
import java.util.*

class FaceArFragment : ArFragment(), MaskSelectedListener, ImageCaptureListener {
    private var faceRegionsRenderable: ModelRenderable? = null

    var faceNodeMap = HashMap<AugmentedFace, AugmentedFaceNode>()
    private var changeModel: Boolean = false
    private var glasses: ArrayList<ModelRenderable> = ArrayList()
    lateinit var faceArView : View
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
                "wow.sfb"))
            .build()
            .thenAccept { modelRenderable ->
                glasses.add(modelRenderable)
                modelRenderable.isShadowCaster = false
                modelRenderable.isShadowReceiver = false
            }
        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse(
                "purple_cat.sfb"))
            .build()
            .thenAccept { modelRenderable ->
                glasses.add(modelRenderable)
                modelRenderable.isShadowCaster = false
                modelRenderable.isShadowReceiver = false
            }
        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse(
                "blue.sfb"))
            .build()
            .thenAccept { modelRenderable ->
                glasses.add(modelRenderable)
                modelRenderable.isShadowCaster = false
                modelRenderable.isShadowReceiver = false
            }
        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse(
                "black.sfb"))
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

    interface Listener {
        fun recordCompleted(file: File)
        fun photoTaken(file: File)
    }

    override fun maskSelected(maskId: Int) {
        changeModel = true
        faceRegionsRenderable = glasses.get(maskId - 1)
    }

    override fun takePhotoClicked() {
        videoRecorder.setSceneView(arSceneView)
        val orientation = resources.configuration.orientation
        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_HIGH, orientation)

        videoRecorder.onToggleRecord(requireContext())

        Handler().postDelayed({
            videoRecorder.onToggleRecord(requireContext())
            (parentFragment as CameraFragment).photoTaken(videoRecorder.videoPath)
        }, 100)

    }

    override fun startVideoClicked() {
        videoRecorder.setSceneView(arSceneView)
        val orientation = resources.configuration.orientation
        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_HIGH, orientation)

        videoRecorder.onToggleRecord(requireContext())
    }

    override fun stopVideoClicked() {
        videoRecorder.onToggleRecord(requireContext())
        (parentFragment as CameraFragment).recordCompleted(videoRecorder.videoPath)
    }
}
