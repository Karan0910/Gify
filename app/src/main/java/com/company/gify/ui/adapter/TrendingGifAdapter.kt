package com.company.gify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.company.gify.R
import com.company.gify.databinding.ItemGifBinding
import com.company.gify.model.Gif


class TrendingGifAdapter(private val gifList: ArrayList<Gif>) : RecyclerView.Adapter<TrendingGifAdapter.GifViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val itemGifBinding: ItemGifBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_gif,
            parent,
            false
        )
        return GifViewHolder(itemGifBinding)
    }

    override fun onBindViewHolder(holder: TrendingGifAdapter.GifViewHolder, position: Int) {

        Glide.with(holder.itemGifBinding.imageViewGif.context)
            .asGif()
            .load(gifList.get(position).images.preview.mp4)
            .into(holder.itemGifBinding.imageViewGif)
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