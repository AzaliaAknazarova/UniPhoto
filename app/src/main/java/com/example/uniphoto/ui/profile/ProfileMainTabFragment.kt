package com.example.uniphoto.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import kotlinx.android.synthetic.main.fragment_profile_tab.*

class ProfileMainTabFragment: KodeinFragment<ProfileMainTabViewModel>() {

    override val viewModel by viewModel(ProfileMainTabViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
        viewModel.init()
    }

    fun bindViewModel() {
        with(viewModel) {
            bindText(emailText, emailValue)
            bindText(userNameText, userNameValue)

            bindCommand(launchGalleryCommand) {
                castParent<Listener>()?.galleryClicked()
            }
            bindCommand(launchLoginScreenCommand) {
                castParent<Listener>()?.navigateToLogin()
            }
        }
    }

    fun initViews() {
        toGalleryLayout.setOnClickListener {
            viewModel.onToGalleryLayoutClicked()
        }
        logoutTextView.setOnClickListener {
            viewModel.onLogOutButtonClicked()
        }
    }

    interface Listener {
        fun galleryClicked()
        fun navigateToLogin()
    }
}