package ayds.songinfo.moredetails.data.proxy

import ayds.songinfo.moredetails.domain.Card

interface Proxy {
    fun getCard(artistName: String) : Card?
}