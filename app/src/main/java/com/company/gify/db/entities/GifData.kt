package com.company.gify.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob


@Entity(tableName = GifData.TABLE_NAME)
data class GifData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Int,
    @ColumnInfo(name = IMAGEID)
    val imageId: String)

{   companion object {
    const val TABLE_NAME="gif_details"
    const val ID = "id"
    const val IMAGEID= "image_id"
    }
}