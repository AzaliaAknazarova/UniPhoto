package com.example.uniphoto.model

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uniphoto.R
import com.example.uniphoto.ui.CameraViewModel
import kotlinx.android.synthetic.main.item_mask.view.*

/**
 * Created by nigelhenshaw on 2018/01/24.
 */
class MaskItemsListAdapter: RecyclerView.Adapter<MaskItemsListViewHolder>() {

    var items = listOf<CameraViewModel.MaskListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MaskItemsListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_mask, parent, false)
        )

    override fun onBindViewHolder(holder: MaskItemsListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

class MaskItemsListViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    fun bind(item: CameraViewModel.MaskListItem) {
        Log.d("tag", "on MaskItemsListViewHolder bind $item")
        itemView.textView.text = item.id.toString()

        itemView.setOnClickListener {
            item.onItemClicked?.invoke(adapterPosition)
        }
    }
}