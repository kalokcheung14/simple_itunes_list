package com.kalok.simpleituneslist.ui.bookmarks

import android.util.Log
import com.kalok.simpleituneslist.repositories.DatabaseHelper
import com.kalok.simpleituneslist.ui.home.HomeViewModel
import com.kalok.simpleituneslist.viewmodels.AlbumViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class BookmarksViewModel : HomeViewModel() {
    fun fetchAlbum() {
        // Get database album DAO and get all albums
        DatabaseHelper.getAlbumDao()?.apply {
            getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ albumResponse ->
                    // Get album list from response and replace the current list
                    val albumList = ArrayList<AlbumViewModel>()
                    // Add all loaded albums to an array list
                    albumList.addAll(albumResponse.map { album ->
                        // Encapsulate album with ViewModel
                        AlbumViewModel(
                            album,
                            // Set bookmarked to true since albums loaded from database are all bookmarked
                            true,
                            compositeDisposable
                        )
                    })

                    // Update album list
                    albums.value = albumList
                }, {
                    Log.e("album", it.message ?: "No error message")
                }).let {
                    // Store disposable API call in composite disposable object
                    compositeDisposable.add(it)
                }
        }
    }
}