package com.company.gify.db.dao

import androidx.room.*
import com.company.gify.db.entities.GifData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface GifDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGifData(data:GifData) : Completable

    @Query("SELECT * FROM ${GifData.TABLE_NAME}")
    fun getAllRecords():Single<List<GifData>>

    @Delete
    fun removeGifData(gif:GifData) : Completable




}
