package ayds.songinfo.moredetails.data.local.lastFM.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: ArticleEntity)

    @Query("SELECT * FROM Articleentity WHERE artistName LIKE :artistName LIMIT 3")
    fun getArticleByArtistName(artistName: String): List<ArticleEntity>

}