package com.example.uniphoto.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: KodeinFragment<LoginViewModel>(),
    SignInFragment.Listener,
    SignUpFragment.Listener {

    override val viewModel by viewModel(LoginViewModel::class.java)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
    }

    private fun initViews() {
        childFragmentManager.beginTransaction().apply {
            add(R.id.appEntryFragment, SignInFragment())
            commit()
        }
        signInTextView.setOnClickListener {
            if (childFragmentManager.fragments.firstOrNull{fragment -> fragment is SignUpFragment} != null) {
                childFragmentManager.beginTransaction().apply {
                    replace(R.id.appEntryFragment, SignInFragment())
                    commit()
                }
                signInIndicator.setBackgroundResource(R.drawable.ic_top_background_main)
                signUpIndicator.setBackgroundResource(R.color.transparent)
            }
        }
        signUpTextView.setOnClickListener {
            if (childFragmentManager.fragments.firstOrNull{fragment -> fragment is SignInFragment} != null) {
                childFragmentManager.beginTransaction().apply {
                    replace(R.id.appEntryFragment, SignUpFragment())
                    commit()
                }
                signUpIndicator.setBackgroundResource(R.drawable.ic_top_background_main)
                signInIndicator.setBackgroundResource(R.color.transparent)
            }
        }
    }

    private fun bindViewModel() {
        with(viewModel) {
            bind(progressBarVisible) {
                progressBar.isVisible = it
                appEntryFragment.alpha = if (it) 0.4f else 1.0f
            }
        }
    }

    override fun signIn(userName: String, password: String) {
        viewModel.singIn(userName, password)
    }

    override fun signUp(userName: String, email: String, password: String) {
        viewModel.signUp(userName, email, password)
    }

}