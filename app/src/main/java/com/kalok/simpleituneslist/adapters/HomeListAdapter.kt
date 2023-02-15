package com.kalok.simpleituneslist.adapters

import androidx.lifecycle.ViewModel
import com.kalok.simpleituneslist.models.Album
import com.kalok.simpleituneslist.repositories.BookmarkRepository

class HomeListAdapter(
    albums: ArrayList<Album>,
    _parentViewModel: ViewModel? = null,
    _bookmarkRepository: BookmarkRepository
): AlbumAdapter(
    albums,
    _parentViewModel,
    _bookmarkRepository
)  {
    override fun handleRemoveBookmark(album: Album, position: Int) = notifyItemChanged(position)
}