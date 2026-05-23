package ayds.songinfo.home.model.repository.local.spotify.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SongEntity::class], version = 3)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
}