package com.kalok.simpleituneslist.viewmodels

import android.util.Log
import com.kalok.simpleituneslist.models.Album
import com.kalok.simpleituneslist.repositories.DatabaseHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AlbumViewModel(
    val album: Album,
    bookmarked: Boolean,
    private val _compositeDisposable: CompositeDisposable?,
    private val _databaseHelper: DatabaseHelper,
) {
    val albumName = album.collectionName
    val artworkUrl = album.artworkUrl60

    var bookmarked: Boolean = bookmarked
        set(value) {
            // Set bookmarked flag in view model
            field = value

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
                        if (field) {
                            insert(album)
                        } else {
                            delete(album)
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.d("album", "bookmark = $field")
                    }, {
                        Log.e("album", it.message ?: "No error message")
                    }).let {
                        // Add disposable call into composite disposable object for disposal
                        _compositeDisposable?.add(it)
                    }
        }
    }
}