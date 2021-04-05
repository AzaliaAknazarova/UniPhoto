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

    @GET("/files/{page}")
    suspend fun getUserContentFiles(
        @Header("Authorization") token: String,
        @Path("page") page: Int
    ): List<ContentData>

    @GET("/all-files/{page}")
    suspend fun getAllContentFiles(
        @Header("Authorization") token: String,
        @Path("page") page: Int
    ): List<ContentData>

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