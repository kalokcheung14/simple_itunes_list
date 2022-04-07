package com.kalok.simpleituneslist.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kalok.simpleituneslist.models.Album
import com.kalok.simpleituneslist.repositories.ApiDataRepository
import com.kalok.simpleituneslist.repositories.DatabaseHelper
import com.kalok.simpleituneslist.viewmodels.AlbumViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val _repo: ApiDataRepository,
    private val _dbHelper: DatabaseHelper,
    private val _compositeDisposable: CompositeDisposable
) : ViewModel() {
    private var albums = MutableLiveData<ArrayList<AlbumViewModel>>()
    val albumValue: LiveData<ArrayList<AlbumViewModel>>
        get() = albums

    init {
        // Init mutable data value
        albums.value = ArrayList()
    }

    fun fetchData() {
        var bookmarkAlbumList: List<Album> = emptyList()
        var networkAlbumList: List<Album> = emptyList()
        // Subscribe to database call response
        _dbHelper.getAlbumDao()?.apply {
            getAll()
            .flatMapObservable { albumList ->
                bookmarkAlbumList = albumList
                
                // Get API response for albums
                _repo.getAlbums().map { dataResult ->
                    networkAlbumList = dataResult.results
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Get a list of collection ID of bookmarked albums
                val bookmarkCollectionIdList = bookmarkAlbumList.map { it.collectionId }

                // Get album list from response and replace the current list
                val albumList = ArrayList<AlbumViewModel>()

                // add result to an array list
                albumList.addAll(networkAlbumList.map { album ->
                    // Encapsulate album with ViewModel
                    AlbumViewModel(
                        album,
                        // Update bookmarked flag based on whether the album is also contained in the bookmarked database
                        bookmarkCollectionIdList.contains(album.collectionId),
                        _compositeDisposable,
                        _dbHelper
                    )
                })

                // Update list
                albums.value = albumList
            }, {
                // Handle error
                Log.d("album", it.message ?: "No error message")
            }).let {
                // Store disposable API call in composite disposable object
                _compositeDisposable.add(it)
            }
        }
    }

    override fun onCleared() {
        // Dispose Data streams
        _compositeDisposable.clear()
        _compositeDisposable.dispose()
        super.onCleared()
    }
}