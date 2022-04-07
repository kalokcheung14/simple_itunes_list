package com.kalok.simpleituneslist.repositories

import android.content.Context
import androidx.room.Room

class DatabaseHelper(context: Context) {
    private val _databaseName: String = "albums_db"
    private var _db: ItunesDatabase? = null

    init {
        // Get DB instance
        _db = Room.databaseBuilder(
            context,
            ItunesDatabase::class.java,
            _databaseName
        ).build()
    }

    fun getAlbumDao(): AlbumDao? {
        // Get album DAO
        return _db?.albumDao()
    }
}