package com.example.uniphoto.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uniphoto.Application.Companion.applicationContext
import com.example.uniphoto.R
import com.example.uniphoto.model.dataClasses.ContentData
import kotlinx.android.synthetic.main.item_feed.view.*

class FeedItemsListAdapter: RecyclerView.Adapter<FeedItemsListViewHolder>() {

    var items = listOf<ContentData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FeedItemsListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        )

    override fun onBindViewHolder(holder: FeedItemsListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

class FeedItemsListViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    fun bind(item: ContentData) {
        itemView.userNameTextView.text = item.userName
        Glide.with(applicationContext())
            .load(item.file)
            .into(itemView.photoImageView)

//        itemView.setOnClickListener {
//            item.onItemClicked?.invoke(adapterPosition)
//        }
    }
}