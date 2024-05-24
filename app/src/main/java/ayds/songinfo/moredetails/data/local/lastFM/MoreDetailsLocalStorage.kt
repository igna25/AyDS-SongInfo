package ayds.songinfo.moredetails.data.local.lastFM

import ayds.songinfo.moredetails.domain.Card

interface MoreDetailsLocalStorage {
    fun getDetailsByArtistName(artistName: String): List<Card>

    fun insertArtistDetails(card: Card)
}