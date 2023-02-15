package com.kalok.simpleituneslist.adapters

import androidx.lifecycle.ViewModel
import com.kalok.simpleituneslist.models.Album
import com.kalok.simpleituneslist.repositories.BookmarkRepository
import com.kalok.simpleituneslist.ui.bookmarks.BookmarksViewModel

class BookmarkListAdapter(
    albums: ArrayList<Album>,
    _parentViewModel: ViewModel? = null,
    _bookmarkRepository: BookmarkRepository
): AlbumAdapter(
    albums,
    _parentViewModel,
    _bookmarkRepository
) {
    override fun handleRemoveBookmark(album: Album, position: Int) {
        _albums.remove(album)
        notifyDataSetChanged()
        // Update parent UI by posting data to parent view model
        if (_albums.isEmpty()) {
            (_parentViewModel as BookmarksViewModel).setAlbumView(_albums)
        }
    }
}