package com.jabg.modulo6p1.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jabg.modulo6p1.data.db.model.SongEntity
import com.jabg.modulo6p1.utils.Constants

@Dao
interface SongDao {

    //create
    @Insert
    suspend fun insertSong(song: SongEntity)

    //read
    @Query("SELECT * FROM ${Constants.DB_SONG_TABLE}")
    suspend fun getAllSongs(): MutableList<SongEntity>

    //update
    @Update
    suspend fun updateSong(song: SongEntity)

    //delete
    @Delete
    suspend fun deleteSong(song: SongEntity)

}