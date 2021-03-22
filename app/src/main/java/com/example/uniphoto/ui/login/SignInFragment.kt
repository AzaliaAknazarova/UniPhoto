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
import kotlinx.android.synthetic.main.fragment_signin.*

class SignInFragment : KodeinFragment<SignInViewModel>() {

    override val viewModel by viewModel(SignInViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
    }

    private fun initViews() {
        signInButton.setOnClickListener {
            viewModel.onSignInButtonClicked()
            val manager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        }
    }

    private fun bindViewModel() {
        with(viewModel) {
            bindTextTwoWay(userNameText, userNameValue)
            bindTextTwoWay(passwordText, passwordValue)

            bind(progressBarVisible) {
                progressBar.isVisible = it
                signInButton.isVisible = !it
            }

            bindCommand(setUserNameError) {
                userNameValue.error = getString(R.string.login_requared_field)
            }
            bindCommand(setPasswordError) {
                passwordValue.error = getString(R.string.login_requared_field)
            }
        }
    }

}