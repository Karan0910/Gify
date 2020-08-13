package com.company.gify.model

data class GifsResult(
    val `data`: List<Gif>,
    val meta: Meta,
    val pagination: Pagination
)