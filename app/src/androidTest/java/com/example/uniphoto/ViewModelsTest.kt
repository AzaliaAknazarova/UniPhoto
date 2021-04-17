package com.example.uniphoto

import androidx.test.platform.app.InstrumentationRegistry
import com.example.uniphoto.api.RequestsApi
import com.example.uniphoto.base.kodein.KodeinApplication
import com.example.uniphoto.base.utils.Utils
import com.example.uniphoto.model.dataClasses.*
import com.example.uniphoto.model.repository.AuthorizationRepository
import com.example.uniphoto.ui.camera.CameraViewModel
import com.example.uniphoto.ui.feed.FeedMainTabViewModel
import com.example.uniphoto.ui.login.LoginViewModel
import com.example.uniphoto.ui.login.SignInViewModel
import com.example.uniphoto.ui.login.SignUpViewModel
import com.example.uniphoto.ui.photoView.PhotoViewViewModel
import com.example.uniphoto.ui.readyPhoto.ReadyPhotoViewModel
import com.example.uniphoto.ui.trial.TrialViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito
import retrofit2.HttpException
import java.io.File
import kotlin.test.assertFailsWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ViewModelsTest {

    private val cameraViewModel by lazy { CameraViewModel(getKodeinForTest()) }
    private val loginViewModel by lazy { LoginViewModel(getKodeinForTest()) }
    private val signInViewModel by lazy { SignInViewModel(getKodeinForTest()) }
    private val signUpViewModel by lazy { SignUpViewModel(getKodeinForTest()) }
    private val trialViewModel by lazy { TrialViewModel(getKodeinForTest()) }
    private val feedViewModel by lazy { FeedMainTabViewModel(getKodeinForTest()) }
    private val readyPhotoViewModel by lazy { ReadyPhotoViewModel(getKodeinForTest()) }

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

    @Test
    fun testSignIn() {
        runBlocking {
            val testUserData = UserData(
                username = "Paulina123",
                password = "Paulina123$"
            )
            val testToken = "testToken123"

            launch(Dispatchers.Main) {
                Mockito.`when`(mock.signIn(testUserData)).thenReturn(TokenResponse(token = testToken))
                loginViewModel.singIn(testUserData.username, testUserData.password!!)
            }
        }
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

    @Test
    fun testSignUp() {
        runBlocking {
            val testUserData = UserData(
                email = "test_user1@gmail.com",
                username = "test_user1",
                password = "test_user1"
            )

            launch(Dispatchers.Main) {
                Mockito.`when`(mock.signUp(testUserData)).thenReturn(testUserData)
                loginViewModel.signUp(testUserData.username, testUserData.email!!, testUserData.password!!)
            }
        }
    }

//    Trial tests
    @Test
    fun testCheckTrial() {
        runBlocking {
            val testToken = "9240484516c2e8364c29402000ebd12f52ed73be"
            val testTrialCheckoutResponse = TrialCheckoutResponse(25)

            launch(Dispatchers.Main) {
                Mockito.`when`(mock.checkTrial("Token $testToken")).thenReturn(testTrialCheckoutResponse)
                trialViewModel.checkTrialPeriod()
            }
        }
    }

    @Test
    fun testIsUserSignIn() {
        val testString = "testString123"
        Utils.putTokenInSharedPref(testString)
        assertTrue(trialViewModel.isUserSignIn())
    }

    @Test
    fun testIsNotUserSignIn() {
        Utils.clearSharedPreferences()
        assertFalse(trialViewModel.isUserSignIn())
    }

    @Test
    fun testToLoginButtonClicked() {
        val testString = "testString123"
        Utils.putTokenInSharedPref(testString)
        trialViewModel.onToLoginButtonClicked()
        assertTrue(Utils.getTokenFromSharedPref().isEmpty())
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
    fun testGetAllFiles() {
        runBlocking {
            val testFile = File("${getContext().filesDir}/UniPhoto", "paulina123.mp4")
            val contentPage = FilesPage(contentCount = 1, content = listOf(ContentData(testFile.path, "10-10-2019")))
            val testToken = "9240484516c2e8364c29402000ebd12f52ed73be"

            launch(Dispatchers.Main) {
                Mockito.`when`(mock.getAllContentFiles("Token $testToken", 1)).thenReturn(contentPage)
                feedViewModel.getContentPage(1)

//                Mockito.verify(mock).getAllContentFiles("Token $testToken", 1)
            }
        }
    }

    private val mock = Mockito.mock(RequestsApi::class.java)
    private val authorizationRepositoryMock = AuthorizationRepository(mock)

    fun getContext() = InstrumentationRegistry.getInstrumentation().targetContext
    fun getKodeinForTest() = (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as KodeinApplication).kodein
}
