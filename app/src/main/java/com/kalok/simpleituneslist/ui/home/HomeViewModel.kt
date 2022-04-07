package com.kalok.simpleituneslist.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.kalok.simpleituneslist.models.Album
import com.kalok.simpleituneslist.repositories.DatabaseHelper
import com.kalok.simpleituneslist.repositories.RetrofitApiDataRepository
import com.kalok.simpleituneslist.viewmodels.AlbumViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.schedulers.Schedulers

open class HomeViewModel : ViewModel() {
    protected var albums = MutableLiveData<ArrayList<AlbumViewModel>>()
    val albumValue: LiveData<ArrayList<AlbumViewModel>>
        get() = albums
    protected var compositeDisposable = CompositeDisposable()

    init {
        // Init mutable data value
        albums.value = ArrayList()
    }

    fun fetchAlbums() {
        // Get repo
        val repo = RetrofitApiDataRepository.api

        // Subscribe to API call response
        var networkAlbumList: List<Album> = emptyList()
        repo.getAlbums()
            .subscribeOn(Schedulers.io())
            .concatMap {
                // Save API album list
                networkAlbumList = it.results

                // Read bookmarked albums from database
                DatabaseHelper.getAlbumDao()!!.getAll().toObservable()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ bookmarkList ->
                // Get album list from response and replace the current list
                val albumList = ArrayList<AlbumViewModel>()
                // add result to an array list
                albumList.addAll(networkAlbumList.map { album ->
                    // Encapsulate album with ViewModel
                    AlbumViewModel(
                        album,
                        // Update bookmarked flag based on whether the album is also contained in the bookmarked database
                        bookmarkList.contains(album),
                        compositeDisposable
                    )
                })

                // Update list
                    albums.value = albumList
            }, {
                // Handle error
                Log.d("album", it.message ?: "No error message")
            }).let {
                // Store disposable API call in composite disposable object
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        // Dispose API call
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}