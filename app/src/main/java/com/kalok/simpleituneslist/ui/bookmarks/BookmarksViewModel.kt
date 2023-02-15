package com.kalok.simpleituneslist.ui.bookmarks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kalok.simpleituneslist.models.Album
import com.kalok.simpleituneslist.repositories.BookmarkRepository
import com.kalok.simpleituneslist.repositories.DatabaseHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val _dbHelper: DatabaseHelper,
    private val _compositeDisposable: CompositeDisposable,
    val bookmarkRepository: BookmarkRepository
) : ViewModel() {
    private var albums = MutableLiveData<ArrayList<Album>>()
    val albumValue: LiveData<ArrayList<Album>>
        get() = albums

    init {
        // Init mutable data value
        albums.value = ArrayList()
    }

    fun fetchData() {
        // Get database album DAO and get all albums
        _dbHelper.getAlbumDao()?.apply {
            getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ albumResponse ->
                    // Get album list from response and replace the current list
                    val albumList = ArrayList<Album>()
                    // Add all loaded albums to an array list
                    albumList.addAll(albumResponse)

                    // Update album list
                    albums.value = albumList
                }, {
                    Log.e("album", it.message ?: "No error message")
                }).let {
                    // Store disposable API call in composite disposable object
                    _compositeDisposable.add(it)
                }
        }
    }

    fun setAlbumView(albums: ArrayList<Album>) {
        this.albums.value = albums
    }

    override fun onCleared() {
        // Dispose API call
        _compositeDisposable.dispose()
        _compositeDisposable.clear()
        super.onCleared()
    }
}