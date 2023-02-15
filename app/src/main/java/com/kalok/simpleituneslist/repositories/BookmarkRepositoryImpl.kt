package com.kalok.simpleituneslist.repositories

import android.util.Log
import com.kalok.simpleituneslist.models.Album
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class BookmarkRepositoryImpl(
    private val _databaseHelper: DatabaseHelper,
    private val _compositeDisposable: CompositeDisposable
): BookmarkRepository {
    override fun updateBookmark(album: Album) {
        // Update bookmarked album to database
        _databaseHelper.getAlbumDao()?.apply {
            // Get bookmarked album from database by collection ID
            getByCollectionId(album.collectionId)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable {
                    // If album is found in bookmark
                    // update the album object with the ID for insert/delete function to find the record
                    if (it.isNotEmpty()) {
                        it[0].let {
                            album.id = it.id
                        }
                    }

                    // Insert if the album is bookmarked
                    // Otherwise delete the album from the database
                    if (album.bookmarked) {
                        insert(album)
                    } else {
                        delete(album)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("album", "bookmark = ${album.bookmarked}")
                }, {
                    Log.e("album", it.message ?: "No error message")
                }).let {
                    // Add disposable call into composite disposable object for disposal
                    _compositeDisposable.add(it)
                }
        }
    }
}