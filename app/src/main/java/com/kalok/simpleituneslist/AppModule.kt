package com.kalok.simpleituneslist

import android.content.Context
import com.kalok.simpleituneslist.repositories.ApiDataRepository
import com.kalok.simpleituneslist.repositories.DatabaseHelper
import com.kalok.simpleituneslist.repositories.DatabaseHelperImpl
import com.kalok.simpleituneslist.repositories.RetrofitApiDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {
    @Provides
    fun provideApiDataRepository(): ApiDataRepository = RetrofitApiDataRepository()

    @Provides
    fun provideDatabaseHelper(@ApplicationContext context: Context): DatabaseHelper = DatabaseHelperImpl(context)

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
}