package com.example.uniphoto.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinActivity

class MainActivity : KodeinActivity() {
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by lazy {
        provide(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.navigateHostFragment)
//        MainFragment
//            .newInstance(intent?.extras)
//            .showAllowingStateLoss(supportFragmentManager)
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
