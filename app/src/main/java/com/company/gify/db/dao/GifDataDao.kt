package com.company.gify.db.dao

import androidx.lifecycle.LiveData
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

    @Query(value = "SELECT * FROM ${Gif.TABLE_NAME}")
    fun getLiveRecords() : LiveData<List<Gif>>

    @Delete
    fun removeGifData(gif: Gif): Completable
}
