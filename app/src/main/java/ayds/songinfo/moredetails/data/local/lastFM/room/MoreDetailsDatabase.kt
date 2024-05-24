package ayds.songinfo.moredetails.data.local.lastFM.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class], version = 1)
abstract class MoreDetailsDatabase : RoomDatabase() {
    abstract fun ArticleDao(): ArticleDao
}