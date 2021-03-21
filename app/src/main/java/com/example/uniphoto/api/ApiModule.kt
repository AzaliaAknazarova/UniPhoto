package com.example.uniphoto.api

import com.example.uniphoto.model.repository.AuthorizationRepository
import com.example.uniphoto.model.retrofit.AUTHORIZED
import com.example.uniphoto.model.retrofit.retrofitModule
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

val apiModule = Kodein.Module("Api") {
    importOnce(retrofitModule)

    bind() from provider { AuthorizationRepository(instance()) }
    bind() from provider { instance<Retrofit>(AUTHORIZED).create(RequestsApi::class.java) }
}