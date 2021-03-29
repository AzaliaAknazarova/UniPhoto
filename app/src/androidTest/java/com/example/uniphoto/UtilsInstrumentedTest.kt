package com.example.uniphoto

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uniphoto.api.RequestsApi
import com.example.uniphoto.base.kodein.KodeinApplication
import com.example.uniphoto.base.utils.Constants
import com.example.uniphoto.base.utils.Utils
import com.example.uniphoto.model.dataClasses.TokenResponse
import com.example.uniphoto.model.dataClasses.UserData
import com.example.uniphoto.model.repository.AuthorizationRepository
import com.example.uniphoto.model.repository.ContentRepository
import com.example.uniphoto.ui.camera.CameraViewModel
import com.example.uniphoto.ui.login.SignInViewModel
import com.example.uniphoto.ui.login.SignUpViewModel
import kotlinx.coroutines.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.kodein.di.generic.instance
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.io.File

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UtilsInstrumentedTest {

    @Test
    fun testEncoder() {
        val testString = "testString123"
        val encodeString = Utils.encodeData(testString)
        val decodeString = Utils.decodeData(encodeString)
        assertNotEquals(testString, encodeString)
        assertEquals(testString, decodeString)
    }

    @Test
    fun testEncodedTokenPrefs() {
        val testString = "testString123"
        Utils.putTokenInSharedPref(testString)

        val pref = getContext().getSharedPreferences(
            Constants.APP_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val stringFromPrefs = pref.getString(Constants.APP_PREFERENCES_TOKEN, "").toString()

        assertNotEquals(testString, stringFromPrefs)
    }

    @Test
    fun testEncodedTokenPrefsPutGet() {
        val testToken = "testToken123"
        Utils.putTokenInSharedPref(testToken)

        val tokenFromPrefs = Utils.getTokenFromSharedPref()

        assertEquals(testToken, tokenFromPrefs)
    }

    @Test
    fun testClearPrefs() {
        val testString = "testString123"
        Utils.putTokenInSharedPref(testString)
        Utils.clearSharedPreferences()

        val tokenFromPrefs = Utils.getTokenFromSharedPref()

        assertEquals("", tokenFromPrefs)
    }

    @Test
    fun testIsImageFile() {
        val testFileImage = File("${getContext().filesDir}/UniPhoto", "paulina123.jpg")
        val testFileVideo = File("${getContext().filesDir}/UniPhoto", "paulina123.mp4")
        assertTrue(Utils.isImageType(testFileImage))
        assertFalse(Utils.isImageType(testFileVideo))
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
