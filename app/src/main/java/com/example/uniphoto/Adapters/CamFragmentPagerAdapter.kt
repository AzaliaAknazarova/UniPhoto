package com.mobapptuts.kotlin_camera2.Adapters

import androidx.core.app.Fragment
import androidx.core.app.FragmentManager
import androidx.core.app.FragmentPagerAdapter
import com.mobapptuts.kotlin_camera2.Fragments.PreviewFragment

/**
 * Created by nigelhenshaw on 2018/01/24.
 */
class CamFragmentPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        TODO("Fragments will be added in future")
    }

    override fun getCount() = 2
}