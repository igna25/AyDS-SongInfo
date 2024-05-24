package ayds.songinfo.moredetails.data.local.lastFM

import ayds.songinfo.moredetails.domain.Card

interface LastFMLocalStorage {
    fun getArtistBiographyByArtistName(artistName: String): Card?

    fun insertArtistBiography(lastFMCard: Card)
}