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
import com.company.gify.viewmodel.TrendingViewModel

class GifAdapter(private val onItemClickListener: onItemClickListener) : RecyclerView.Adapter<GifAdapter.GifViewHolder>() {


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
        val gif= gifList.get(position)
        holder.itemGifBinding.gif = gif
        Glide.with(holder.itemGifBinding.imageViewGif.context)
            .asGif()
            .load(gifList.get(position).imageURL)
            .into(holder.itemGifBinding.imageViewGif)


        holder.itemGifBinding.favImg.setOnClickListener(View.OnClickListener {
            onItemClickListener.onItemClick(gif)
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