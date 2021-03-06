package com.shahim.starrynight.view.adapter

import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.shahim.starrynight.R
import com.shahim.starrynight.model.ImageObject
import com.squareup.picasso.Picasso


class ImageListQuickAdapter(data: ArrayList<ImageObject>) : BaseQuickAdapter<ImageObject, BaseViewHolder>(R.layout.list_image_item, data) {

    override fun convert( viewHolder: BaseViewHolder, item: ImageObject?) {
        item?.let {
            Picasso.with(context)
                .load(it.url)
                .placeholder(R.drawable.bg_placeholder)
                .into(viewHolder.getView(R.id.list_image) as ImageView)

            ViewCompat.setTransitionName(viewHolder.getView(R.id.list_image),it.title.replace("\\s".toRegex(), ""))
        }
    }
   fun updateItems(newItems: List<ImageObject>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return data.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].title == newItems[newItemPosition].title
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return true
            }
        })
        diffResult.dispatchUpdatesTo(this)
        this.data.clear()
        this.data.addAll(newItems)
    }
}