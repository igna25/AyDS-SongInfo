package ayds.songinfo.moredetails.data.proxy

import ayds.artist.external.lastfm.data.ArtistBiography
import ayds.songinfo.moredetails.domain.Card

const val SOURCE = "LastFM"
interface LastFMProxy {
    fun getCard(artistName: String) : Card?
}

internal class LastFMProxyImpl(private val lastFMArticleService: ayds.artist.external.lastfm.data.LastFMArticleService): LastFMProxy{

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

