package com.kalok.simpleituneslist.repositories

import com.kalok.simpleituneslist.models.DataResult
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

class AlbumRepository {
    lateinit var api: AlbumApi
    var cachedAlbum = DataResult(0, emptyList())

    fun getAlbums(): Observable<DataResult> {
        return if (cachedAlbum.results.isEmpty()) {
            // Return albums from API
            api.getAlbums().doOnNext {
                cachedAlbum = it
            }
        } else {
            // Return cached albums first
            // then API data
            Observable
                .just(cachedAlbum)
                .mergeWith(api.getAlbums())
                .doOnNext {
                    cachedAlbum = it
                }
        }
    }
}

interface AlbumApi {
    @GET("search?term=jack+johnson&entity=album")
    fun getAlbums(): Observable<DataResult>
}