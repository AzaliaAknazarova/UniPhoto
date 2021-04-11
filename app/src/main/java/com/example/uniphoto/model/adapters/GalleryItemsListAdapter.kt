package com.example.uniphoto.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uniphoto.R
import com.example.uniphoto.ui.galery.GalleryViewModel
import kotlinx.android.synthetic.main.item_gallery.view.*

class GalleryItemsListAdapter: RecyclerView.Adapter<GalleryItemsListViewHolder>() {

    var items = listOf<GalleryViewModel.GalleryListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GalleryItemsListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        )

    override fun onBindViewHolder(holder: GalleryItemsListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

class GalleryItemsListViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    fun bind(item: GalleryViewModel.GalleryListItem) {
        itemView.nameTextView.text = item.name
        itemView.dateTextView.text = item.date
        item.imageView.into(itemView.previewImageView)

        itemView.setOnClickListener {
            item.onItemClicked?.invoke(adapterPosition)
        }
    }
}