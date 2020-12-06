package com.example.uniphoto.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
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
import java.util.*

class FaceArFragment : ArFragment() {
    private var faceRegionsRenderable: ModelRenderable? = null

    var faceNodeMap = HashMap<AugmentedFace, AugmentedFaceNode>()
    private var changeModel: Boolean = false

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

        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse("yellow_sunglasses.sfb"))
            .build()
            .thenAccept { modelRenderable ->
                faceRegionsRenderable = modelRenderable
                modelRenderable.isShadowCaster = false
                modelRenderable.isShadowReceiver = false
            }

        val sceneView = this.arSceneView
        sceneView?.cameraStreamRenderPriority = Renderable.RENDER_PRIORITY_FIRST
        val scene = sceneView?.scene

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
}