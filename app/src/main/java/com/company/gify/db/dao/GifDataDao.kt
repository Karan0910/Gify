package com.company.gify.db.dao

import androidx.room.*
import com.company.gify.db.entities.Gif
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface GifDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGifData(data: Gif): Completable

    @Query("SELECT * FROM ${Gif.TABLE_NAME}")
    fun getAllRecords(): Single<List<Gif>>

    @Delete
    fun removeGifData(gif: Gif): Completable
}
