package com.kalok.simpleituneslist.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kalok.simpleituneslist.models.Album
import com.kalok.simpleituneslist.repositories.NetworkAlbumRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel : ViewModel() {
    private var _albums = MutableLiveData<ArrayList<Album>>()
    val albumValue: LiveData<ArrayList<Album>>
        get() = _albums
    private var myCompositeDisposable: CompositeDisposable? = null

    init {
        myCompositeDisposable = CompositeDisposable()
        // Init mutable data value
        _albums.value = ArrayList()
    }

    fun fetchAlbums() {
        // Get repo
        val repo = NetworkAlbumRepository.instance.api

        // Subscribe to API call response
        myCompositeDisposable?.add(repo.getAlbums()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ albumResponse ->
                // Get album list from response
                _albums.value = albumResponse.results
            }, {
                // Handle error
                Log.d("album", it.message!!)
            }))
    }
}