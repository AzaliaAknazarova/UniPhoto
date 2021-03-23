package com.example.uniphoto.model.repository

import com.example.uniphoto.api.RequestsApi
import com.example.uniphoto.base.utils.Utils
import com.example.uniphoto.model.dataClasses.TrialCheckoutResponse
import com.example.uniphoto.model.dataClasses.UserData

class AuthorizationRepository(private val requestsApi: RequestsApi) {

    suspend fun signUp(userData: UserData) =
            requestsApi.signUp(userData)

    suspend fun signIn(userData: UserData) =
            requestsApi.signIn(userData)

    suspend fun checkTrial() : TrialCheckoutResponse {
        val token = Utils.getTokenFromSharedPref()
        return requestsApi.checkTrial(token)
    }

}