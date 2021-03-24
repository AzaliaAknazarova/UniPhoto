package com.example.uniphoto.ui.trial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import kotlinx.android.synthetic.main.fragment_login.progressBar
import kotlinx.android.synthetic.main.fragment_trial.*

class TrialFragment: KodeinFragment<TrialViewModel>() {

    override val viewModel by viewModel(TrialViewModel::class.java)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
        viewModel.init()
    }

    private fun initViews() {
        toMainButton.setOnClickListener {
            viewModel.onToMainButtonClicked()
        }
        toLoginButton.setOnClickListener {
            viewModel.onToLoginButtonClicked()
        }
    }

    private fun bindViewModel() {
        with(viewModel) {
            bindText(titleText, title)
            bindText(subtitleText, subtitle)

            bind(progressBarVisible) {
                progressBar.isVisible = it
                title.isVisible = !it
                subtitle.isVisible = !it
            }
            bind(navigateToMainButtonVisible) {
                toMainButton.isVisible = it
                toLoginButton.isVisible = !it
            }

            bindCommand(launchLoginScreenCommand) {
                navigate(R.id.action_trialFragment_to_loginFragment)
            }
            bindCommand(launchMainScreenCommand) {
                navigate(R.id.action_trialFragment_to_mainFragment)
            }
        }
    }

}
