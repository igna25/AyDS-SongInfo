package ayds.songinfo.moredetails.data.external.lastFM

import ayds.songinfo.moredetails.domain.ArtistBiography

interface LastFMArticleService {
    fun getArtistBiography(artistName: String): ArtistBiography?
}