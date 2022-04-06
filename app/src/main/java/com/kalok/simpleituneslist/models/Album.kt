package com.kalok.simpleituneslist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class Album(
    @PrimaryKey(autoGenerate = false)
    val collectionId: Long,
    @ColumnInfo(name = "album_name")
    val collectionName: String,
    @ColumnInfo(name = "artwork_url")
    val artworkUrl60: String,
)