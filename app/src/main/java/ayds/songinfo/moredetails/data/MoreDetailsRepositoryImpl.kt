package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.local.lastFM.MoreDetailsLocalStorage
import ayds.songinfo.moredetails.domain.MoreDetailsRepository
import ayds.songinfo.moredetails.domain.Card

class MoreDetailsRepositoryImpl(
    private val moreDetailsLocalStorage: MoreDetailsLocalStorage,
    private val broker: Broker,
): MoreDetailsRepository {
    override fun getDetailsByArtistName(artistName: String): List<Card> {
        var cards = moreDetailsLocalStorage.getDetailsByArtistName(artistName)

        if (cards.isNotEmpty()){
            cards.forEach{
                it.markItAsLocal()
            }
        }
        else {
            cards = broker.getCards(artistName)

            cards.forEach {
                if (it.description.isNotEmpty()) {
                    moreDetailsLocalStorage.insertArtistDetails(it)
                }
            }
        }
        if (cards.isEmpty()) {
            cards = listOf(Card(artistName, "", "", "", ""))
        }
        return cards
    }

    private fun Card.markItAsLocal() {
        isLocallyStored = true
    }
}