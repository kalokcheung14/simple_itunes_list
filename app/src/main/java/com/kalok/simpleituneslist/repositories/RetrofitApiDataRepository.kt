package com.kalok.simpleituneslist.repositories

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApiDataRepository: ApiDataRepository() {
    private const val BASE_URL = "https://itunes.apple.com"

    init {
        // Create API instance using Retrofit and RxJava
        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(DataApi::class.java)
    }
}