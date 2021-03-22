package com.example.uniphoto.model.repository

import com.example.uniphoto.api.RequestsApi
import com.example.uniphoto.model.dataClasses.UserData

class AuthorizationRepository(private val requestsApi: RequestsApi) {

    suspend fun signUp(userData: UserData) =
            requestsApi.signUp(userData)

    suspend fun signIn(userData: UserData) =
            requestsApi.signIn(userData)
}