package com.jabg.modulo6p1.data

import com.jabg.modulo6p1.data.db.SongDao
import com.jabg.modulo6p1.data.db.model.SongEntity

class SongRepository (private val songDao: SongDao){

    suspend fun insertSong(song: SongEntity){
        songDao.insertSong(song)
    }

    suspend fun getAllSongs(): MutableList<SongEntity> =
        songDao.getAllSongs()

    suspend fun updateSong(song: SongEntity){
        songDao.updateSong(song)
    }

    suspend fun deleteSong(song: SongEntity){
        songDao.deleteSong(song)
    }

}