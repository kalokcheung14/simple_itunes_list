package com.kalok.simpleituneslist

import android.content.Context
import com.kalok.simpleituneslist.repositories.*
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

    @Provides
    fun provideBookmarkRepository(databaseHelper: DatabaseHelper, compositeDisposable: CompositeDisposable): BookmarkRepository = BookmarkRepositoryImpl(databaseHelper, compositeDisposable)
}