package com.example.uniphoto.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: KodeinFragment<LoginViewModel>() {

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
            Log.d("tag", "on initViews ${childFragmentManager.fragments}")
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
            Log.d("tag", "on initViews ${childFragmentManager.fragments}")
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

        }
    }

}