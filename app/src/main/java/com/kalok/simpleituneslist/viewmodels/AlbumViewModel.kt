package com.kalok.simpleituneslist.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kalok.simpleituneslist.models.Album
import com.kalok.simpleituneslist.repositories.DatabaseHelper
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AlbumViewModel(
    val album: Album,
    bookmarked: Boolean,
    private val compositeDisposable: CompositeDisposable?) {
    val albumName = album.collectionName
    private val artworkUrl = album.artworkUrl60
    val artwork: RequestCreator?
        get() =
            // Use Picasso to load image from URL
            Picasso.get()
                .load(artworkUrl)
                .placeholder(android.R.color.darker_gray)
                .fit()
    private var _bookmarked = MutableLiveData<Boolean>()
    val bookmarkedValue: LiveData<Boolean>
        get() = _bookmarked

    init {
        _bookmarked.value = bookmarked
    }

    fun setBookmark(bookmarked: Boolean) {
        // Set bookmarked flag in view model
        _bookmarked.value = bookmarked

        // Update bookmarked album to database
        DatabaseHelper.getAlbumDao()?.apply {
            // Insert if the album is bookmarked
            // Otherwise delete the album from the database
            if (bookmarked) {
                insert(album)
            } else {
                delete(album)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("album", "bookmark = $bookmarked")
                }, {
                    Log.e("album", it.message ?: "No error message")
                }).let {
                    // Add disposable call into composite disposable object for disposal
                    compositeDisposable?.add(it)
                }
        }
    }
}