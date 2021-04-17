package com.example.uniphoto.model.repository

import com.example.uniphoto.api.RequestsApi
import com.example.uniphoto.base.utils.Utils
import okhttp3.MediaType.Companion.get
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ContentRepository(private val requestsApi: RequestsApi) {

    suspend fun postContentFile(file: File, token: String) {
        val filePart = MultipartBody.Part.createFormData(
            "file", file.name, file.asRequestBody(
                if (file.name.contains(".jpg"))
                    "image/*".toMediaType()
                else
                    "video/*".toMediaType()
            )
        )
        requestsApi.postContentFile("Token $token", filePart)
    }

    suspend fun getUserContentFiles(token: String, page: Int) =
        requestsApi.getUserContentFiles("Token $token", page)

    suspend fun getFeedContentFiles(token: String, page: Int) =
        requestsApi.getAllContentFiles("Token $token", page)

    suspend fun getUserData(token: String) =
        requestsApi.getUserDetails("Token $token")

}