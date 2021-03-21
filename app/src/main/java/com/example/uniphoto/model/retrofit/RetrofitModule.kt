package com.example.uniphoto.model.retrofit

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

const val AUTHORIZED = "Retrofit"
const val BASE_URL = "http://192.168.0.100:8000/"

val retrofitModule = Kodein.Module("RetrofitModule") {
    bind<String>() with singleton { BASE_URL }
    bind<Retrofit>(AUTHORIZED) with singleton { RetrofitFactory.create(instance()) }
}
