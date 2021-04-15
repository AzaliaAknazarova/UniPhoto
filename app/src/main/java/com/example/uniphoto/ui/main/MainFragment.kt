package com.example.uniphoto.ui.main

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import com.example.uniphoto.ui.camera.CameraFragment
import com.example.uniphoto.ui.feed.FeedMainTabFragment
import com.example.uniphoto.ui.galery.GalleryFragment
import com.example.uniphoto.ui.main.MainFragment.MainTab.Camera
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
        viewModel.init()
    }

    override fun onResume() {
        super.onResume()

    }

    fun bindViewModel() {
        with(viewModel) {
            bindCommand(switchScreen) { index ->
                bottomNavigationView.itemActiveIndex = index
                Handler().postDelayed({
                    val tab = MainTab.getByIndex(index)
                    childFragmentManager.beginTransaction()
                        .apply {
                            setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            childFragmentManager.fragments.forEach { hide(it) }
                            val addedFragment =
                                childFragmentManager.fragments.firstOrNull { fragment -> fragment.tag == tab.tag }
                            if (addedFragment != null) {
                                show(addedFragment)
                            } else {
                                val fragment = MainTab.getByIndex(index).fragmentCreator()
                                add(R.id.containerFrameLayout, fragment, tab.tag)
                            }
                        }
                        .commitAllowingStateLoss()
                }, 200)
                bottomBarMaterialCardView.isVisible = index != Camera.index
            }
            bindCommand(launchGalleryScreen) {
                navigate(R.id.action_mainFragment_to_galleryFragment)
            }
            bindCommand(launchReadyPhotoScreen) {
                val arg = Bundle().apply { putString(GalleryFragment.galleryFragmentArg, it) }
                navigate(R.id.action_mainFragment_to_readyPhotoFragment, arg)
            }
            bindCommand(launchLoginScreen) {
                navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }
    }

    fun initViews() {
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

    override fun navigateToLogin() {
        viewModel.navigateToLogin()
    }

    enum class MainTab(
        val fragmentCreator: () -> Fragment,
        val index: Int,
        val tag: String
    ) {
        Feed({ FeedMainTabFragment() }, 0, FeedMainTabFragment::class.java.name),
        Camera({ CameraFragment() }, 1, CameraFragment::class.java.name),
        Profile({ ProfileMainTabFragment() }, 2, ProfileMainTabFragment::class.java.name);

        companion object {
            fun getByIndex(index: Int): MainTab = values().first { it.index == index }
        }
    }
}