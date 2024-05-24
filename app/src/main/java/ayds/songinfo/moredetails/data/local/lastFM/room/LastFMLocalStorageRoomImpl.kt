package ayds.songinfo.moredetails.data.local.lastFM.room

import ayds.songinfo.moredetails.data.local.lastFM.LastFMLocalStorage
import ayds.songinfo.moredetails.domain.Card

internal class LastFMLocalStorageRoomImpl(
    database: ArticleDatabase
): LastFMLocalStorage {

    private val articleDao = database.ArticleDao()

    override fun getArtistBiographyByArtistName(artistName: String): Card? {
        return articleDao.getArticleByArtistName(artistName)?.toArtistCard()
    }

    override fun insertArtistBiography(lastFMCard: Card) {
        articleDao.insertArticle(lastFMCard.toArticleEntity())
    }

    private fun ArticleEntity.toArtistCard() = Card(
        this.artistName,
        this.biography,
        this.articleUrl,
        "LastFM",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    )

    private fun Card.toArticleEntity() = ArticleEntity(
        this.artistName,
        this.description,
        this.infoUrl,
    )
}