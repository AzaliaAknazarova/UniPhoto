package com.example.uniphoto.model

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uniphoto.R
import com.example.uniphoto.ui.camera.CameraViewModel
import com.example.uniphoto.ui.galery.GalleryViewModel
import kotlinx.android.synthetic.main.item_mask.view.*
import kotlinx.android.synthetic.main.item_video.view.*

/**
 * Created by nigelhenshaw on 2018/01/24.
 */
class VideoItemsListAdapter: RecyclerView.Adapter<VideoItemsListViewHolder>() {

    var items = listOf<GalleryViewModel.VideoListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VideoItemsListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        )

    override fun onBindViewHolder(holder: VideoItemsListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

class VideoItemsListViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    fun bind(item: GalleryViewModel.VideoListItem) {
        itemView.nameTextView.text = item.name
        itemView.dateTextView.text = item.date
        item.imageView.into(itemView.previewImageView)

        itemView.setOnClickListener {
            item.onItemClicked?.invoke(adapterPosition)
        }
    }
}