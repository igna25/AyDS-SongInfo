package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.proxy.Proxy
import ayds.songinfo.moredetails.domain.Card


interface Broker {
    fun getCards(artisName: String) : List<Card>
}
internal class BrokerImpl(private val proxies: List<Proxy>): Broker {
    override fun getCards(artisName: String): List<Card> {
        val cards = mutableListOf<Card>()
         proxies.forEach{
            it.getCard(artisName)?.let {
                card -> cards.add(card)
            }
        }
        return cards
    }
}