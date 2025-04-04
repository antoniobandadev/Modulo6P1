package com.jabg.modulo6p1.application

import android.app.Application
import com.jabg.modulo6p1.data.SongRepository
import com.jabg.modulo6p1.data.db.SongDatabase

class SongsDBApp: Application() {
    private val database by lazy {
        SongDatabase.getDatabase(this@SongsDBApp)
    }

    val repository by lazy {
        SongRepository(database.songDao())
    }

}