package com.example.uniphoto.api

import com.example.uniphoto.model.dataClasses.FileItem
import com.example.uniphoto.model.dataClasses.TokenResponse
import com.example.uniphoto.model.dataClasses.TrialCheckoutResponse
import com.example.uniphoto.model.dataClasses.UserData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.io.File

interface RequestsApi {

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

    @Multipart
    @POST("/post-file")
    suspend fun postContentFile(
        @Header("Authorization") token: String,
        @Part filePart : MultipartBody.Part
    )

    @GET("/files/{page}")
    suspend fun getUserContentFiles(
        @Header("Authorization") token: String,
        @Path("page") page: Int
    ): List<FileItem>

    @GET("/user-details")
    suspend fun getUserDetails(
        @Header("Authorization") token: String
    ): UserData

//    @POST("/poffers/v0/{uid}/ride")
//    suspend fun getTripOffers(
//        @Header("Authorization") userToken: String,
//        @Path("uid") uId: Long,
//        @Body request: RidePath
//    ): List<JsonObject>

}