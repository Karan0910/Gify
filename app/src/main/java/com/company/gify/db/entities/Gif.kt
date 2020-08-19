package com.company.gify.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Gif.TABLE_NAME)
data class Gif(

    @PrimaryKey
    @ColumnInfo(name = ID)
    var id: String = ""
) {
    @ColumnInfo(name = IMAGEURL)
    var imageURL = ""
    var isFavorite = false

    companion object {
        const val TABLE_NAME = "gif_details"
        const val ID = "id"
        const val IMAGEURL = "image_url"
    }
}