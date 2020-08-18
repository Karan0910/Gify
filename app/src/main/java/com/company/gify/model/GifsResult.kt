package com.company.gify.model

data class GifsResult(
    val `data`: List<GifResponse>,
    val meta: Meta,
    val pagination: Pagination
)