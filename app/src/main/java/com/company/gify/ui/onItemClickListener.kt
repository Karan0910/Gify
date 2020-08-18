package com.company.gify.ui

import androidx.lifecycle.ViewModel
import com.company.gify.db.entities.Gif

interface onItemClickListener {
    fun onItemClick(gif: Gif)
}