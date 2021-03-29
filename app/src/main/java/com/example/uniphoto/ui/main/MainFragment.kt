package com.example.uniphoto.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import com.example.uniphoto.ui.camera.CameraFragment
import com.example.uniphoto.ui.feed.FeedMainTabFragment
import com.example.uniphoto.ui.galery.GalleryFragment
import com.example.uniphoto.ui.profile.ProfileMainTabFragment
import com.example.uniphoto.ui.readyPhoto.ReadyPhotoFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment: KodeinFragment<MainViewModel>(),
    CameraFragment.Listener,
    ProfileMainTabFragment.Listener,
    ReadyPhotoFragment.Listener {

    override val viewModel by viewModel(MainViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (childFragmentManager.fragments.firstOrNull{fragment -> fragment is ProfileMainTabFragment} != null)
            bottomNavigationView.itemActiveIndex = 2
    }

    fun bindViewModel() {
        with(viewModel) {
            bindCommand(switchScreen) { index ->
                bottomBarMaterialCardView.isVisible = true
                bottomNavigationView.itemActiveIndex = index
                when(index) {
                    0 -> {
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.containerFrameLayout, FeedMainTabFragment())
                            commit()
                        }
                    }
                    1 -> {
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.containerFrameLayout, CameraFragment())
                            commit()
                        }
                        bottomBarMaterialCardView.isVisible = false
                    }
                    2 -> {
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.containerFrameLayout, ProfileMainTabFragment())
                            commit()
                        }
                    }
                }
            }
            bindCommand(launchGalleryScreen) {
                navigate(R.id.action_mainFragment_to_galleryFragment)
            }
            bindCommand(launchReadyPhotoScreen) {
                val arg = Bundle().apply { putString(GalleryFragment.galleryFragmentArg, it) }
                navigate(R.id.action_mainFragment_to_readyPhotoFragment, arg)
            }
        }
    }

    fun initViews() {
        if (childFragmentManager.fragments.firstOrNull() == null) {
            childFragmentManager.beginTransaction().apply {
                add(R.id.containerFrameLayout, FeedMainTabFragment())
                commit()
            }
        }

        bottomNavigationView.onItemSelected = { index ->
            viewModel.onBottomNavigationClicked(index)
        }
    }

    override fun cameraBackPressed() {
        viewModel.onCameraBackPressed()
    }

    override fun onOpenReadyPhotoScreen(arg: String) {
        viewModel.onReadyPhotoOpen(arg)
    }

    override fun galleryClicked() {
        viewModel.onGalleryClicked()
    }

    override fun onReadyPhotoClosed() {
        viewModel.onReadyPhotoClosed()
    }
}