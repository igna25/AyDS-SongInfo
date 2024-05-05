package ayds.songinfo.moredetails.data.local.lastFM.room

import ayds.songinfo.moredetails.data.local.lastFM.LastFMLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography

internal class LastFMLocalStorageRoomImpl(
    database: ArticleDatabase
): LastFMLocalStorage {

    private val articleDao = database.ArticleDao()

    override fun getArtistBiographyByArtistName(artistName: String): ArtistBiography? {
        return articleDao.getArticleByArtistName(artistName)?.toArtistBiography()
    }

    override fun insertArtistBiography(artistBiography: ArtistBiography) {
        articleDao.insertArticle(artistBiography.toArticleEntity())
    }

    private fun ArticleEntity.toArtistBiography() = ArtistBiography(
        this.artistName,
        this.biography,
        this.articleUrl
    )

    private fun ArtistBiography.toArticleEntity() = ArticleEntity(
        this.artistName,
        this.biography,
        this.articleUrl
    )
}