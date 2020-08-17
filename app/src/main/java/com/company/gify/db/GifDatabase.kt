package com.company.gify.db

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.company.gify.db.dao.GifDataDao
import com.company.gify.db.entities.GifData


@Database(entities = [GifData::class], version = DB_VERSION, exportSchema = false)
abstract class GifDatabase : RoomDatabase() {
    abstract fun gifDataDao(): GifDataDao

    companion object {
        @Volatile
        private var databseInstance: GifDatabase? = null

        fun getDatabasenInstance(mContext: Context): GifDatabase =
            databseInstance ?: synchronized(this) {
                databseInstance ?: buildDatabaseInstance(mContext).also {
                    databseInstance = it
                }
            }

        private fun buildDatabaseInstance(mContext: Context) =
            Room.databaseBuilder(mContext, GifDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

    }
}


const val DB_VERSION = 1

const val DB_NAME = "GidDataDatabase.db"