package com.example.uniphoto.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment

class FeedMainTabFragment: KodeinFragment<FeedMainTabViewModel>() {

    override val viewModel by viewModel(FeedMainTabViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    fun bindViewModel() {
        with(viewModel) {

        }
    }

    fun initViews() {

    }
}