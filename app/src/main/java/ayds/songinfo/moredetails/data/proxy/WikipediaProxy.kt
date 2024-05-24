package ayds.songinfo.moredetails.data.proxy

import ayds.artist.external.wikipedia.data.WikipediaArticle
import ayds.artist.external.wikipedia.data.WikipediaTrackService
import ayds.songinfo.moredetails.domain.Card

private const val SOURCE = "Wikipedia"

internal class WikipediaProxy(private val wikipediaTrackService: WikipediaTrackService): Proxy{

    override fun getCard(artistName: String) : Card? {
        val wikipediaArticle = wikipediaTrackService.getInfo(artistName)
        return wikipediaArticle?.toCard(artistName)
    }

    private fun WikipediaArticle.toCard(artistName: String) = Card(
        artistName,
        description,
        wikipediaURL,
        SOURCE,
        wikipediaLogoURL,
    )
}