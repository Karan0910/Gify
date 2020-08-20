package com.company.gify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.company.gify.R
import com.company.gify.databinding.ItemGifBinding
import com.company.gify.db.entities.Gif
import com.company.gify.ui.onItemClickListener

class GifAdapter(private val onItemClickListener: onItemClickListener) :
    RecyclerView.Adapter<GifAdapter.GifViewHolder>() {

    private val gifList = ArrayList<Gif>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val itemGifBinding: ItemGifBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_gif,
            parent,
            false
        )
        return GifViewHolder(itemGifBinding)
    }

    override fun onBindViewHolder(holder: GifAdapter.GifViewHolder, position: Int) {
        val gif = gifList.get(position)
        holder.itemGifBinding.gif = gif
        Glide.with(holder.itemGifBinding.imageViewGif.context)
            .asGif()
            .load(gifList.get(position).imageURL)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.itemGifBinding.imageViewGif)

        if (gif.isFavorite)
            holder.itemGifBinding.favImg.progress = 1F
        else {
            holder.itemGifBinding.favImg.progress = 0F
        }

        holder.itemGifBinding.favImg.setOnClickListener(View.OnClickListener {
            if (gif.isFavorite) {
                holder.itemGifBinding.favImg.progress = 0F
                onItemClickListener.onItemClick(gif)
            } else {
                holder.itemGifBinding.favImg.progress = 1F
                onItemClickListener.onItemClick(gif)

            }
        })
        holder.itemGifBinding.executePendingBindings()
    }

    inner class GifViewHolder(val itemGifBinding: ItemGifBinding) :
        RecyclerView.ViewHolder(itemGifBinding.root)

    fun setUpGifs(listOfGifs: List<Gif>) {
        gifList.clear()
        gifList.addAll(listOfGifs)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = gifList.size
}