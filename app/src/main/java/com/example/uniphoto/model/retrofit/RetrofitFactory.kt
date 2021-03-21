package com.example.uniphoto.model.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {
    private val loggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    fun create(baseUrl: String): Retrofit =
        createRetrofit(createOkHttpClient(), baseUrl)

    private fun createOkHttpClient(token: String? = null) =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .run {
                if (token != null) addInterceptor(
                    createAuthorizationInterceptor(token)
                ) else this
            }
            .build()

    private fun createRetrofit(client: OkHttpClient, baseUrl: String) =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    private fun createAuthorizationInterceptor(token: String) = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.request()
            .newBuilder()
            .addHeader("Authorization", token)
            .build()
            .let(chain::proceed)
    }
}
