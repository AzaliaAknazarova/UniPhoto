package com.example.uniphoto.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinActivity
import com.example.uniphoto.base.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : KodeinActivity() {
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by lazy {
        provide(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.navigateHostFragment)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

//        val mainFragment = supportFragmentManager.fragments
//            .firstOrNull { it is MainFragment } as? MainFragment
//
//        intent?.extras?.let {
//            mainFragment?.onNotificationClicked(it)
//        }
    }
}
