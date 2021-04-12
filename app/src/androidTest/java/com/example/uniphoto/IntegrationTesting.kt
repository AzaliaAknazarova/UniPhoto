package com.example.uniphoto

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import com.example.uniphoto.base.kodein.KodeinApplication
import com.example.uniphoto.model.dataClasses.ContentData
import com.example.uniphoto.model.dataClasses.FilesPage
import com.example.uniphoto.model.dataClasses.UserData
import com.example.uniphoto.model.repository.AuthorizationRepository
import com.example.uniphoto.model.repository.ContentRepository
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import org.kodein.di.generic.instance
import org.mockito.Mockito
import retrofit2.HttpException
import java.io.File
import kotlin.test.assertFailsWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class IntegrationTesting {

    @Rule @JvmField
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val authorizationRepository by getKodeinForTest().instance<AuthorizationRepository>()
    private val contentRepository by getKodeinForTest().instance<ContentRepository>()

    var tokenSuccess = ""

    @Test
    fun testSignUpSuccess() {
        runBlocking {
            val testUserData = UserData(
                email = "PaulinaPaulina@gmail.com",
                username = "PaulinaPaulina",
                password = "Paulina123$"
            )

            launch(Dispatchers.Main) {
                val testSignUp = authorizationRepository.signUp(testUserData)
                assertEquals(UserData(email = "PaulinaPaulina@gmail.com", username = "PaulinaPaulina"), testSignUp)
            }
        }
    }

    @Test
    fun testSignUpFailUserExist() {
        runBlocking {
            val testUserData = UserData(
                email = "PaulinaPaulina@gmail.com",
                username = "PaulinaPaulina",
                password = "Paulina123$"
            )

            launch(Dispatchers.Main) {
                assertFailsWith<HttpException> { authorizationRepository.signUp(testUserData) }
            }
        }
    }

    @Test
    fun testSignUpFailEmptyFields() {
        runBlocking {
            val testUserData = UserData(
                email = "",
                username = "",
                password = ""
            )

            launch(Dispatchers.Main) {
                assertFailsWith<HttpException> { authorizationRepository.signUp(testUserData) }
            }
        }
    }

    @Test
    fun testSignInSuccess() {
        runBlocking {
            val testUserData = UserData(
                username = "PaulinaPaulina",
                password = "Paulina123$"
            )

            launch(Dispatchers.Main) {
                tokenSuccess = authorizationRepository.signIn(testUserData).token
                assertTrue(tokenSuccess.isNotEmpty())
            }
        }
    }

    @Test
    fun testSignInFailWrongPassword() {
        runBlocking {
            val testUserData = UserData(
                username = "Paulina123$",
                password = "P"
            )

            launch(Dispatchers.Main) {
                assertFailsWith<HttpException> { authorizationRepository.signIn(testUserData) }
            }
        }
    }

    @Test
    fun testSignInFailEmptyFields() {
        runBlocking {
            val testUserData = UserData(
                username = "",
                password = ""
            )

            launch(Dispatchers.Main) {
                assertFailsWith<HttpException> { authorizationRepository.signIn(testUserData) }
            }
        }
    }

    @Test
    fun testSignUpSignInSuccess() {
        runBlocking {
            tokenSuccess = ""
            val testUserData = UserData(
                email = "PaulinaPaulina@gmail.com",
                username = "PaulinaPaulina",
                password = "Paulina123$"
            )

            launch(Dispatchers.Main) {
                authorizationRepository.signUp(testUserData)
                tokenSuccess = authorizationRepository.signIn(testUserData).token
                assertTrue(tokenSuccess.isNotEmpty())
            }
        }
    }

    @Test
    fun testCheckTrialSuccess() {
        //        testSignInSuccess()
        runBlocking {
            launch(Dispatchers.Main) {
                val testCheckTrial = authorizationRepository.checkTrial(tokenSuccess)
                assertTrue(testCheckTrial.daysToEnd >= 0)
            }
        }
    }

    @Test
    fun testCheckTrialFail() {
        runBlocking {
            val testToken = "testToken123"

            launch(Dispatchers.Main) {
                assertFailsWith<HttpException> { authorizationRepository.checkTrial(testToken) }
            }
        }
    }

    @Test
    fun testUserDataSuccess() {
        testSignInSuccess()
        runBlocking {
            val testUserData = UserData(
                email = "PaulinaPaulina@gmail.com",
                username = "PaulinaPaulina")

            launch(Dispatchers.Main) {
                val testGetData = contentRepository.getUserData(tokenSuccess)
                assertEquals(testUserData, testGetData)
            }
        }
    }

    @Test
    fun testUserDataFail() {
        runBlocking {
            val testToken = "testToken123"

            launch(Dispatchers.Main) {
                assertFailsWith<HttpException> { contentRepository.getUserData(testToken) }
            }
        }
    }

//    @Test
//    fun testPostImageSuccess() {
//        testSignInSuccess()
//        runBlocking {
//            val testFileImage = File("${getInstrumentation().context.filesDir}/UniPhoto", "image_test.jpg")
//
//            launch(Dispatchers.Main) {
//                contentRepository.postContentFile(testFileImage, tokenSuccess)
//            }
//        }
//    }

    @Test
    fun testGetFeedContentPageSuccess() {
        testSignInSuccess()
        runBlocking {
            launch(Dispatchers.Main) {
                val contentPage = contentRepository.getFeedContentFiles(tokenSuccess, 1)
                assertTrue(!contentPage.content.isNullOrEmpty())
            }
        }
    }

    @Test
    fun testGetFeedContentPageFail() {
        runBlocking {
            val testToken = "testToken123"

            launch(Dispatchers.Main) {
                assertFailsWith<HttpException> { contentRepository.getFeedContentFiles(testToken, 1) }
            }
        }
    }

    @Test
    fun testGetUserContentPageSuccess() {
        testSignInSuccess()
        runBlocking {
            launch(Dispatchers.Main) {
                val contentPage = contentRepository.getUserContentFiles(tokenSuccess, 1)
                assertTrue(contentPage.content.isNullOrEmpty())
            }
        }
    }

    @Test
    fun testGetUserContentPageFail() {
        runBlocking {
            val testToken = "testToken123"

            launch(Dispatchers.Main) {
                assertFailsWith<HttpException> { contentRepository.getUserContentFiles(testToken, 1) }
            }
        }
    }

    fun getContext() = getInstrumentation().targetContext
    fun getKodeinForTest() = (getInstrumentation().targetContext.applicationContext as KodeinApplication).kodein
}
