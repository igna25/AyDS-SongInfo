package ayds.songinfo.moredetails.data.local.lastFM.room

import androidx.room.Entity

@Entity(primaryKeys = ["artistName", "source"])
data class ArticleEntity(
    val artistName: String,
    val source: String,
    val description: String,
    val articleUrl: String,
    val sourceLogoUrl: String,
)