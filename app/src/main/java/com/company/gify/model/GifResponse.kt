package com.company.gify.model

import com.company.gify.db.entities.Gif

data class GifResponse(
    val id: String,
    val images: Images,
    val import_datetime: String
)

fun mapToGif(gifResponse: GifResponse): Gif {
    val gif = Gif(gifResponse.id)
    gif.imageURL = gifResponse.images.preview_gif.url
    return gif
}
