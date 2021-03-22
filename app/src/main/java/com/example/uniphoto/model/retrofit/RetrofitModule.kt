package com.example.uniphoto.model.retrofit

import com.example.uniphoto.base.utils.Constants.AUTHORIZED
import com.example.uniphoto.base.utils.Constants.BASE_URL
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

val retrofitModule = Kodein.Module("RetrofitModule") {
    bind<String>() with singleton { BASE_URL }
    bind<Retrofit>(AUTHORIZED) with singleton { RetrofitFactory.create(instance()) }
}
