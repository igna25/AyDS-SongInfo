package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.local.lastFM.LastFMLocalStorage
import ayds.songinfo.moredetails.data.proxy.LastFMProxy
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository
import ayds.songinfo.moredetails.domain.Card

class ArtistBiographyRepositoryImpl(
    private val lastFMLocalStorage: LastFMLocalStorage,
    private val proxyLastFm: LastFMProxy,
): ArtistBiographyRepository {
    override fun getAristBiographyByArtistName(artistName: String): Card {
        var card = lastFMLocalStorage.getArtistBiographyByArtistName(artistName)

        if (card != null){
            card.markItAsLocal()
        }
        else {
            card = proxyLastFm.getCard(artistName)
            if (card != null){
                if (card.description.isNotEmpty()) {
                    lastFMLocalStorage.insertArtistBiography(card)
                }
            }
            else {
                card = Card(artistName, "", "", "", "")
            }
        }

        return card
    }

    private fun Card.markItAsLocal() {
        isLocallyStored = true
    }
}