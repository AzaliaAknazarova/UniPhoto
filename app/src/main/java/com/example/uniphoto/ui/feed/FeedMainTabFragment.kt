package com.example.uniphoto.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import com.example.uniphoto.model.adapters.FeedItemsListAdapter
import kotlinx.android.synthetic.main.fragment_feed_tab.*

class FeedMainTabFragment: KodeinFragment<FeedMainTabViewModel>() {

    override val viewModel by viewModel(FeedMainTabViewModel::class.java)

    private val feedItemsAdapter = FeedItemsListAdapter()

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
        bindViewModel()
        viewModel.init()
    }

    fun bindViewModel() {
        with(viewModel) {

            bind(feedItemsList) {
                feedItemsAdapter.items = it
                feedItemsAdapter.notifyDataSetChanged()
            }
        }
    }

    fun initViews() {
        feedItemsRecyclerView.adapter = feedItemsAdapter
        feedItemsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.reachTheEndOfRecycler()
                }
            }
        })
    }
}