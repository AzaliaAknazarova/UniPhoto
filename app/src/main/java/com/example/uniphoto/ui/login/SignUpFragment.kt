package com.example.uniphoto.ui.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.passwordValue

class SignUpFragment : KodeinFragment<SignUpViewModel>() {

    override val viewModel by viewModel(SignUpViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
    }

    private fun initViews() {
        signUpButton.setOnClickListener {
            viewModel.onSignUpButtonClicked()
            val manager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        }
    }

    private fun bindViewModel() {
        with(viewModel) {
            bindTextTwoWay(userNameText, usernameValue)
            bindTextTwoWay(emailText, emailValue)
            bindTextTwoWay(passwordText, passwordValue)

            bind(progressBarVisible) {
                progressBar.isVisible = it
                signUpButton.isVisible = !it
            }

            bindCommand(setUserNameError) {
                usernameValue.error = getString(R.string.login_requared_field)
            }
            bindCommand(setEmailError) {
                emailValue.error = getString(R.string.login_invalid_email_value)
            }
            bindCommand(setPasswordError) {
                passwordValue.error = getString(R.string.login_invalid_password_value)
            }
        }
    }

}