package com.example.uniphoto.api

import com.example.uniphoto.model.dataClasses.TokenResponse
import com.example.uniphoto.model.dataClasses.TrialCheckoutResponse
import com.example.uniphoto.model.dataClasses.UserData
import retrofit2.http.*

interface RequestsApi {

    @Headers("Content-Type: application/json")
    @POST("/registration")
    suspend fun signUp(
            @Body userData: UserData
    )

    @Headers("Content-Type: application/json")
    @POST("/api-token-auth")
    suspend fun signIn(
            @Body userData: UserData
    ): TokenResponse

    @GET("/trial-license-check")
    suspend fun checkTrial(
        @Header("Authorization") token: String
    ): TrialCheckoutResponse

//    @POST("/poffers/v0/{uid}/ride")
//    suspend fun getTripOffers(
//        @Header("Authorization") userToken: String,
//        @Path("uid") uId: Long,
//        @Body request: RidePath
//    ): List<JsonObject>

}