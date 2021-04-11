package com.example.uniphoto

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uniphoto.base.kodein.KodeinApplication
import com.example.uniphoto.base.utils.Constants
import com.example.uniphoto.base.utils.Utils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
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
    fun testClearTokenFromPrefs() {
        val testString = "testString123"
        Utils.putTokenInSharedPref(testString)
        Utils.removeTokenFromSharedPref()

        val tokenFromPrefs = Utils.getTokenFromSharedPref()

        assertEquals("", tokenFromPrefs)
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

    fun getContext() = InstrumentationRegistry.getInstrumentation().targetContext
    fun getKodeinForTest() = (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as KodeinApplication).kodein
}
