package com.example.uniphoto.api

import com.example.uniphoto.model.dataClasses.SignUpResponse
import com.example.uniphoto.model.dataClasses.UserData
import retrofit2.http.*

interface RequestsApi {

    @Headers("Content-Type: application/json")
    @POST("/registration")
    suspend fun signUp(
        @Body userData: UserData
    ): SignUpResponse

//    @POST("/poffers/v0/{uid}/ride")
//    suspend fun getTripOffers(
//        @Header("Authorization") userToken: String,
//        @Path("uid") uId: Long,
//        @Body request: RidePath
//    ): List<JsonObject>

}