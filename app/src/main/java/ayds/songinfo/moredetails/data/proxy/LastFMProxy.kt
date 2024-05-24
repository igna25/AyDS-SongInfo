package ayds.songinfo.moredetails.data.proxy

import ayds.artist.external.lastfm.data.ArtistBiography
import ayds.artist.external.lastfm.data.LastFMArticleService
import ayds.songinfo.moredetails.domain.Card

private const val SOURCE = "LastFM"

internal class LastFMProxy(private val lastFMArticleService: LastFMArticleService): Proxy{

    override fun getCard(artistName: String) : Card? {
        val artistBiography = lastFMArticleService.getArtistBiography(artistName)

        return artistBiography?.toCard()
    }

    private fun ArtistBiography.toCard() = Card(
        artistName,
        biography,
        articleUrl,
        SOURCE,
        logoUrl,
    )
}

