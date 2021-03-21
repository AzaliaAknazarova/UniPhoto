package com.example.uniphoto.model.repository

import android.content.Context
import com.example.uniphoto.api.RequestsApi
import com.example.uniphoto.model.dataClasses.UserData

class AuthorizationRepository(
    private val requestsApi: RequestsApi
//    , private val authService: AuthService
    ) {

    suspend fun signUp(userData: UserData) =
        requestsApi.signUp(userData)

}