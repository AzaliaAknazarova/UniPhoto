package com.example.uniphoto

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uniphoto.api.RequestsApi
import com.example.uniphoto.base.kodein.KodeinApplication
import com.example.uniphoto.model.dataClasses.TokenResponse
import com.example.uniphoto.model.dataClasses.TrialCheckoutResponse
import com.example.uniphoto.model.dataClasses.UserData
import com.example.uniphoto.model.repository.AuthorizationRepository
import com.example.uniphoto.model.repository.ContentRepository
import kotlinx.coroutines.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.mockito.Mockito
import org.mockito.Mockito.`when`

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RepositoryInstrumentedTest {

    private val mock = Mockito.mock(RequestsApi::class.java)
    private val authorizationRepository = AuthorizationRepository(mock)
    private val contentRepository = ContentRepository(mock)

    @Test
    fun testRepositorySignIn() {
        runBlocking {
            val testUserData = UserData(
                username = "Paulina123$",
                password = "Paulina123$"
            )
            val testToken = "testToken123"

            launch(Dispatchers.Main) {
                `when`(mock.signIn(testUserData)).thenReturn(TokenResponse(token = testToken))
                val testSignIn = authorizationRepository.signIn(testUserData).token
                assertEquals(testToken, testSignIn)
            }
        }
    }

    @Test
    fun testRepositorySignUp() {
        runBlocking {
            val testUserData = UserData(
                email = "Paulina123$@gmail.com",
                username = "Paulina123$",
                password = "Paulina123$"
            )

            launch(Dispatchers.Main) {
                `when`(mock.signUp(testUserData)).thenReturn(testUserData)
                val testSignUp = authorizationRepository.signUp(testUserData)
                assertEquals(testUserData, testSignUp)
            }
        }
    }

    @Test
    fun testRepositoryCheckTrial() {
        runBlocking {
            val testToken = "testToken123"
            val testTrialCheckoutResponse = TrialCheckoutResponse(25)

            launch(Dispatchers.Main) {
                `when`(mock.checkTrial("Token $testToken")).thenReturn(testTrialCheckoutResponse)
                val testCheckTrial = authorizationRepository.checkTrial(testToken)
                assertEquals(testTrialCheckoutResponse, testCheckTrial)
            }
        }
    }



    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.uniphoto", appContext.packageName)
    }

    fun getContext() = InstrumentationRegistry.getInstrumentation().targetContext
    fun getKodeinForTest() = (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as KodeinApplication).kodein
}
