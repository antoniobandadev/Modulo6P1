package com.jabg.modulo6p1.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jabg.modulo6p1.utils.Constants

@Entity(tableName = Constants.DB_SONG_TABLE)
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "songId")
    var id : Long = 0,
    @ColumnInfo(name = "songTitle")
    var title: String,
    @ColumnInfo(name = "songArtist")
    var artist: String,
    @ColumnInfo(name = "songAlbum")
    var album: String,
    @ColumnInfo(name = "songGenre")
    var genre: String,
    @ColumnInfo(name = "songYear")
    var year: Int,
    @ColumnInfo(name = "songDuration")
    var durationSec: Int
)
