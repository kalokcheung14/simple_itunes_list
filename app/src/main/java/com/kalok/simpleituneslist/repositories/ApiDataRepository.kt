package com.kalok.simpleituneslist.repositories

import com.kalok.simpleituneslist.models.DataResult
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

abstract class ApiDataRepository {
    lateinit var api: DataApi

    var cachedAlbum = DataResult(0, ArrayList())

    fun getAlbums(): Observable<DataResult> {
        return if (cachedAlbum.results.isEmpty()) {
            // Return albums from API
            api.getAlbums().doOnNext {
                cachedAlbum = it
            }
        } else {
            // Return cached albums
            Observable
                .just(cachedAlbum)
        }
    }
}

interface DataApi {
    @GET("search?term=jack+johnson&entity=album")
    fun getAlbums(): Observable<DataResult>
}