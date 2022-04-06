package com.kalok.simpleituneslist.repositories

import android.content.Context
import androidx.room.Room

object DatabaseHelper {
    private const val DATABASE_NAME: String = "albums_db"
    private var db: AlbumDatabase? = null

    fun setDb(context: Context) {
        // Get DB instance
        db = Room.databaseBuilder(
            context, AlbumDatabase::class.java, DATABASE_NAME
        )
            .build()
    }

    fun getAlbumDao(): AlbumDao? {
        // Get album DAO
        return db?.albumDao()
    }
}