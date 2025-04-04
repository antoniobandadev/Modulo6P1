package com.jabg.modulo6p1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jabg.modulo6p1.data.db.model.SongEntity
import com.jabg.modulo6p1.utils.Constants

@Database(
    entities = [SongEntity::class],
    version = 1,
    exportSchema = true
)

abstract class SongDatabase : RoomDatabase(){

    abstract fun songDao(): SongDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getDatabase(context: Context): SongDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        Constants.DB_NAME
                    ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}