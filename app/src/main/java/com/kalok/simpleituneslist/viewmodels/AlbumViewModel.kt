package com.kalok.simpleituneslist.viewmodels

import com.kalok.simpleituneslist.models.Album
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

class AlbumViewModel(album: Album) {
    val albumName = album.collectionName
    private val artworkUrl = album.artworkUrl60
    val artwork: RequestCreator?
        get() =
            // Use Picasso to load image from URL
            Picasso.get()
                .load(artworkUrl)
                .placeholder(android.R.color.darker_gray)
                .fit()
    var bookmarked = album.bookmarked
}