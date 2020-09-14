package com.company.gify.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.company.gify.db.entities.Gif
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface GifDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGifData(data: Gif)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGifList(gifList: List<Gif>)

    @Query("SELECT * FROM ${Gif.TABLE_NAME}")
    fun getAllRecords(): Single<List<Gif>>

    @Query(value = "SELECT * FROM ${Gif.TABLE_NAME}")
    fun getLiveRecords() : LiveData<List<Gif>>

    @Query(value = "SELECT * FROM ${Gif.TABLE_NAME} WHERE ${Gif.ISFAVORITE} = 1")
    fun getFavGifs() : LiveData<List<Gif>>

    @Delete
    fun removeGifData(gif: Gif)

    @Query("DELETE FROM ${Gif.TABLE_NAME}")
    fun removeAllData() : Completable
}
