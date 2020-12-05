package com.example.uniphoto

import android.content.Context
import com.example.uniphoto.base.kodein.KodeinApplication
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class Application : KodeinApplication(), KodeinAware {
    override val rootModule = Kodein.Module("Root") {
        import(module)
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        private const val APP_PREFERENCES = "sharedpref"
        private var instance: Application? = null

        var appIsShowing: Boolean = false
        var isNotificationsShowing = false

        private val module = Kodein.Module("Application") {
            bind() from singleton {
                instance!!.applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            }
        }

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}