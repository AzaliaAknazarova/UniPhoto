package com.example.uniphoto.model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by nigelhenshaw on 2018/01/24.
 */
class CamFragmentPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        TODO("Fragments will be added in future")
    }

    override fun getCount() = 2
}