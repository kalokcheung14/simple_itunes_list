package com.kalok.simpleituneslist.repositories

import com.kalok.simpleituneslist.models.Album

interface BookmarkRepository {
    fun updateBookmark(album: Album)
}