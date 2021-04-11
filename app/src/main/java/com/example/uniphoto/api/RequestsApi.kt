package com.example.uniphoto.api

import com.example.uniphoto.model.dataClasses.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface RequestsApi {
//  login
    @Headers("Content-Type: application/json")
    @POST("/registration")
    suspend fun signUp(
            @Body userData: UserData
    ): UserData

    @Headers("Content-Type: application/json")
    @POST("/api-token-auth")
    suspend fun signIn(
            @Body userData: UserData
    ): TokenResponse

    @GET("/trial-license-check")
    suspend fun checkTrial(
        @Header("Authorization") token: String
    ): TrialCheckoutResponse

//    content
    @Multipart
    @POST("/post-file")
    suspend fun postContentFile(
        @Header("Authorization") token: String,
        @Part filePart : MultipartBody.Part
    )

    @GET("/user-files?page={page}")
    suspend fun getUserContentFiles(
        @Header("Authorization") token: String,
        @Path("page") page: Int
    ): FilesPage

    @GET("/all-files")
    suspend fun getAllContentFiles(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): FilesPage

    @GET("/user-details")
    suspend fun getUserDetails(
        @Header("Authorization") token: String
    ): UserData

}