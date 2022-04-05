package com.kalok.simpleituneslist.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kalok.simpleituneslist.models.Album
import com.kalok.simpleituneslist.repositories.AlbumApi
import com.kalok.simpleituneslist.repositories.AlbumRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel : ViewModel() {
    private var _albums = MutableLiveData<ArrayList<Album>>()
    val albumValue: LiveData<ArrayList<Album>>
        get() = _albums
    private val repo = AlbumRepository()
    private var myCompositeDisposable: CompositeDisposable? = null

    init {
        myCompositeDisposable = CompositeDisposable()
        _albums.value = ArrayList()
    }

    fun fetchAlbums() {
        val requestInterface = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(AlbumApi::class.java)

        myCompositeDisposable?.add(requestInterface.getAlbums()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ albumResponse ->
                val albums = ArrayList<Album>()
                for (album in albumResponse.results) {
                    albums.add(album)
                }
                _albums.value = albums
            }, {
                Log.d("album", it.message!!)
            }))
    }
}