package com.example.uniphoto

import com.example.uniphoto.ui.camera.CameraViewModel
import com.example.uniphoto.ui.camera.VideoRecorder
import org.junit.Test

import org.junit.Assert.*
import org.kodein.di.Kodein
import org.mockito.Mockito

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

//    private var viewModel by lazy { pro CameraViewModel::class.java }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

//    @Test
//    fun fileNameIsCorrect() {
//        val cameraViewClass = Mockito.mock(CameraViewModel::class.java)
//        val name = cameraViewClass.genPictureName()
//        print("pic_" + System.currentTimeMillis() + ".jpg")
//        assertTrue(name.contains("pic_"))
//    }
//
//    @Test
//    fun fileFormatIsCorrect() {
//        val cameraViewClass = Mockito.mock(CameraViewModel::class.java)
//        cameraViewClass.genPictureName().contains(".jpg")
//    }

}
