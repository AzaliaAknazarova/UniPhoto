package com.example.uniphoto

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uniphoto.base.kodein.KodeinApplication
import com.example.uniphoto.ui.camera.CameraViewModel

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
