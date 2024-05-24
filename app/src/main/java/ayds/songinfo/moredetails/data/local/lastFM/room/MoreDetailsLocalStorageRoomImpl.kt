package ayds.songinfo.moredetails.data.local.lastFM.room

import ayds.songinfo.moredetails.data.local.lastFM.MoreDetailsLocalStorage
import ayds.songinfo.moredetails.domain.Card

internal class MoreDetailsLocalStorageRoomImpl(
    database: MoreDetailsDatabase
): MoreDetailsLocalStorage {

    private val articleDao = database.ArticleDao()

    override fun getDetailsByArtistName(artistName: String): List<Card> {
        return articleDao.getArticleByArtistName(artistName).map {it.toArtistCard()}
    }

    override fun insertArtistDetails(card: Card) {
        articleDao.insertArticle(card.toArticleEntity())
    }

    private fun ArticleEntity.toArtistCard() = Card(
        this.artistName,
        this.description,
        this.articleUrl,
        this.source,
        this.sourceLogoUrl
    )

    private fun Card.toArticleEntity() = ArticleEntity(
        this.artistName,
        this.source,
        this.description,
        this.infoUrl,
        this.sourceLogoUrl
    )
}