package com.example.uniphoto

import androidx.test.platform.app.InstrumentationRegistry
import com.example.uniphoto.base.kodein.KodeinApplication
import com.example.uniphoto.base.utils.Utils
import com.example.uniphoto.ui.camera.CameraViewModel
import com.example.uniphoto.ui.login.LoginViewModel
import com.example.uniphoto.ui.login.SignInViewModel
import com.example.uniphoto.ui.login.SignUpViewModel

import org.junit.Test

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ViewModelsInstrumentedTest {

    private val cameraViewModel by lazy { CameraViewModel(getKodeinForTest()) }
    private val loginViewModel by lazy { LoginViewModel(getKodeinForTest()) }
    private val signInViewModel by lazy { SignInViewModel(getKodeinForTest()) }
    private val signUpViewModel by lazy { SignUpViewModel(getKodeinForTest()) }

    //    SignIn tests
    @Test
    fun testUserNameEmptiness() {
        assertFalse(signInViewModel.isUserNameValid(""))
        assertTrue(signInViewModel.isUserNameValid("PaulinaGirl"))
    }

    @Test
    fun testPasswordEmptiness() {
        assertFalse(signInViewModel.isPasswordValid(""))
        assertTrue(signInViewModel.isPasswordValid("Paulina123$"))
    }

    @Test
    fun testVerifySignInTrue() {
        val testUserName = "Paulina123$"
        val testPassword = "Paulina123$"

        val isVerified = signInViewModel.verify(testUserName, testPassword)
        assertTrue(isVerified)
    }

    @Test
    fun testVerifySignInFalse() {
        val testUserName = ""
        val testPassword = ""

        val isVerified = signInViewModel.verify(testUserName, testPassword)
        assertFalse(isVerified)
    }

    @Test
    fun testPutTokenToSharedPref() {
        val testToken = "Token123"
        loginViewModel.saveAuthorizationToken(testToken)

        val stringFromPrefs = Utils.getTokenFromSharedPref()
        assertEquals(testToken, stringFromPrefs)
    }

//    SignUp tests
    @Test
    fun testUserNameValidity() {
        assertFalse(signUpViewModel.isUserNameValid(""))
        assertTrue(signUpViewModel.isUserNameValid("PaulinaGirl"))
    }

    @Test
    fun testEmailValidity() {
        assertFalse(signUpViewModel.isEmailValid(""))
        assertFalse(signUpViewModel.isEmailValid("P0.com"))
        assertFalse(signUpViewModel.isEmailValid("Pppp0@.com"))
        assertTrue(signUpViewModel.isEmailValid("Paulina123@gmail.com"))
    }

    @Test
    fun testPasswordValidity() {
        assertFalse(signUpViewModel.isPasswordValid(""))
        assertFalse(signUpViewModel.isPasswordValid("P0"))
        assertFalse(signUpViewModel.isPasswordValid("Pppp0"))
        assertTrue(signUpViewModel.isPasswordValid("Paulina123$"))
    }

    @Test
    fun testVerifySignUpTrue() {
        val testUserName = "Paulina123$"
        val testEmail = "Paulina123@gmail.com"
        val testPassword = "Paulina123$"

        val isVerified = signUpViewModel.verify(testUserName,testEmail, testPassword)
        assertTrue(isVerified)
    }

    @Test
    fun testVerifySignUpFalse() {
        val testUserName = ""
        val testEmail = "P0.com"
        val testPassword = "P0"

        val isVerified = signUpViewModel.verify(testUserName, testEmail, testPassword)
        assertFalse(isVerified)
    }

//    Camera tests
    @Test
    fun testGenPictureName() {
        val pictureName = cameraViewModel.genPictureName()
        assertTrue(pictureName.run {
            contains("pic_")
            contains(".jpg")
        })
    }

    fun getContext() = InstrumentationRegistry.getInstrumentation().targetContext
    fun getKodeinForTest() = (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as KodeinApplication).kodein
}
