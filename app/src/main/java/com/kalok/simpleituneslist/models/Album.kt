package com.kalok.simpleituneslist.models

data class Album(
    val collectionName: String,
    val artworkUrl60: String,
    var bookmarked: Boolean = false
    )