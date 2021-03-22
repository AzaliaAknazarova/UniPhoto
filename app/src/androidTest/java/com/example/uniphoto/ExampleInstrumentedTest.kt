package com.example.uniphoto

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uniphoto.base.kodein.KodeinApplication
import com.example.uniphoto.ui.camera.CameraViewModel
import com.example.uniphoto.ui.login.SignInViewModel
import com.example.uniphoto.ui.login.SignUpViewModel

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val cameraViewModel by lazy { CameraViewModel(getKodeinForTest()) }
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

//    Camera tests
    @Test
    fun testGenPictureName() {
        val pictureName = cameraViewModel.genPictureName()
        assertTrue(pictureName.run {
            contains("pic_")
            contains(".jpg")
        })
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.uniphoto", appContext.packageName)
    }

    fun getKodeinForTest() = (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as KodeinApplication).kodein
}
