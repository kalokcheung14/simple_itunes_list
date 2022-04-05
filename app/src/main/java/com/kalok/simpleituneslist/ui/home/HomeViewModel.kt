package com.kalok.simpleituneslist.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.kalok.simpleituneslist.repositories.NetworkAlbumRepository
import com.kalok.simpleituneslist.viewmodels.AlbumViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel : ViewModel() {
    private var _albums = MutableLiveData<ArrayList<AlbumViewModel>>()
    val albumValue: LiveData<ArrayList<AlbumViewModel>>
        get() = _albums
    private var myCompositeDisposable: CompositeDisposable? = null
    private var _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded: LiveData<Boolean>
        get() = _dataLoaded

    init {
        myCompositeDisposable = CompositeDisposable()
        // Init mutable data value
        _albums.value = ArrayList()
        _dataLoaded.value = false
    }

    fun fetchAlbums() {
        // Get repo
        val repo = NetworkAlbumRepository.instance.api

        // Subscribe to API call response
        myCompositeDisposable?.add(repo.getAlbums()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ albumResponse ->
                // Get album list from response and replace the current list
                val albums = ArrayList<AlbumViewModel>()
                albums.addAll(albumResponse.results.map { album ->
                    AlbumViewModel(album)
                })
                _albums.value = albums

                // Notify data loaded
                _dataLoaded.value = true
            }, {
                // Handle error
                Log.d("album", it.message!!)
            }))
    }
}