package com.company.gify.model

import com.company.gify.db.entities.Gif

data class GifResponse(
    val id: String,
    val images: Images,
    val import_datetime: String


)

fun mapToGif(gifResponse: GifResponse) : Gif {
    return Gif(gifResponse.id,gifResponse.images.preview_gif.url)
}
