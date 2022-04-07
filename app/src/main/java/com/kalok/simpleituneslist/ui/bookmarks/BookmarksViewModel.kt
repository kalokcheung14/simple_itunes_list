package com.kalok.simpleituneslist.ui.bookmarks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kalok.simpleituneslist.repositories.DatabaseHelper
import com.kalok.simpleituneslist.ui.home.HomeViewModel
import com.kalok.simpleituneslist.viewmodels.AlbumViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

    init {
        // Init mutable data value
        albums.value = ArrayList()
    }

    fun fetchData() {
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
                            compositeDisposable,
                            DatabaseHelper
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

    override fun onCleared() {
        // Dispose API call
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}