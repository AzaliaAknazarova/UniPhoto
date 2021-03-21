package com.example.uniphoto.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : KodeinFragment<LoginViewModel>() {

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
        signUpButton.setOnClickListener {
            viewModel.onSignUpButtonClicked()
        }
    }

    private fun bindViewModel() {
        with(viewModel) {}
    }

}