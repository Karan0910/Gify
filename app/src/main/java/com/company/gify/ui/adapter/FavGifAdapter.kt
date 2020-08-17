package com.company.gify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.company.gify.R
import com.company.gify.databinding.ItemGifBinding
import com.company.gify.db.entities.GifData
import com.company.gify.model.Gif
import com.company.gify.viewmodel.FavouriteViewModel
import com.company.gify.viewmodel.TrendingViewModel

class FavGifAdapter : RecyclerView.Adapter<FavGifAdapter.GifViewHolder>() {

    private val gifList = ArrayList<GifData>()

    lateinit var viewModel: FavouriteViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val itemGifBinding: ItemGifBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_gif,
            parent,
            false
        )

        itemGifBinding.favViewModel = viewModel
        return GifViewHolder(itemGifBinding)
    }

    override fun onBindViewHolder(holder: FavGifAdapter.GifViewHolder, position: Int) {

        val gif=
            gifList.get(position)
        holder.itemGifBinding.gifData=gif

        Glide.with(holder.itemGifBinding.imageViewGif.context)
            .asGif()
            .load(gifList.get(position).imageURL)
            .into(holder.itemGifBinding.imageViewGif)


        holder.itemGifBinding.favImage.setBackgroundResource(R.drawable.ic_fav_filled)


    }

    inner class GifViewHolder(val itemGifBinding: ItemGifBinding) :
        RecyclerView.ViewHolder(itemGifBinding.root)


    fun setUpGifs(listOfGifs: List<GifData>) {
        gifList.clear()
        gifList.addAll(listOfGifs)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = gifList.size
}