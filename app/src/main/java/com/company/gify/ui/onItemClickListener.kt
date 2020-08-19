package com.company.gify.ui

import com.company.gify.db.entities.Gif

interface onItemClickListener {
    fun onItemClick(gif: Gif)
}