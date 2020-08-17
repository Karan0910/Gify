package com.company.gify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.company.gify.R
import com.company.gify.databinding.ItemGifBinding
import com.company.gify.model.Gif
import com.company.gify.viewmodel.TrendingViewModel

class TrendingGifAdapter : RecyclerView.Adapter<TrendingGifAdapter.GifViewHolder>() {

    private val gifList = ArrayList<Gif>()

    lateinit var viewModel: TrendingViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val itemGifBinding: ItemGifBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_gif,
            parent,
            false
        )

        itemGifBinding.trendingViewModel = viewModel
        return GifViewHolder(itemGifBinding)
    }

    override fun onBindViewHolder(holder: TrendingGifAdapter.GifViewHolder, position: Int) {

        val gif=
            gifList.get(position)
        holder.itemGifBinding.gif=gif

        Glide.with(holder.itemGifBinding.imageViewGif.context)
            .asGif()
            .load(gifList.get(position).images.preview_gif.url)
            .into(holder.itemGifBinding.imageViewGif)

        holder.itemGifBinding.favImg.setCurrentlyLiked(gif.favorite)

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