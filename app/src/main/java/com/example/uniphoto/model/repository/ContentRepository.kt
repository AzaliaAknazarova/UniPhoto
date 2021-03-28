package com.example.uniphoto.model.repository

import android.util.Log
import com.example.uniphoto.api.RequestsApi
import com.example.uniphoto.base.utils.Utils
import com.example.uniphoto.model.dataClasses.TrialCheckoutResponse
import com.example.uniphoto.model.dataClasses.UserData
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ContentRepository(private val requestsApi: RequestsApi) {

    suspend fun postContentFile(file: File) {
        if (file.name.contains(".jpg"))
            postImage(file)
        else
            postVideo(file)

    }

    private suspend fun postImage(file: File) {
        val filePart = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody(
            "image/*".toMediaType()))
        requestsApi.postContentFile(getToken(), filePart)
    }

    private suspend fun postVideo(file: File) {
        val filePart = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody(
            "video/*".toMediaType()))
        requestsApi.postContentFile(getToken(), filePart)
    }

    suspend fun getContentFiles(page: Int) =
            requestsApi.getUserContentFiles(getToken(), page)


    private fun getToken() =
        "Token " + Utils.getTokenFromSharedPref()

}