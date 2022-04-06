package com.kalok.simpleituneslist.repositories

import androidx.room.*
import com.kalok.simpleituneslist.models.Album
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

// Data Access Object
@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums")
    fun getAll(): Single<List<Album>>

    @Query("DELETE FROM albums")
    fun clear(): Completable

    @Update
    fun updateAlbum(album: Album)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(album: Album): Completable

    @Delete
    fun delete(album: Album): Completable
}