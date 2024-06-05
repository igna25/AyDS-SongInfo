package ayds.songinfo.moredetails.data.proxy

import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.data.NYTimesService
import ayds.artist.external.newyorktimes.data.NYT_LOGO_URL
import ayds.songinfo.moredetails.domain.Card

private const val SOURCE = "New York Times"

internal class NewYorkTimesProxy(private val nYTimesService: NYTimesService): Proxy{

    override fun getCard(artistName: String) : Card? {
        val newYorkTimesArticle = nYTimesService.getArtistInfo(artistName)
        return when {
            newYorkTimesArticle is NYTimesArticle.NYTimesArticleWithData -> newYorkTimesArticle.toCard()
            else -> null
        }
    }

    private fun NYTimesArticle.NYTimesArticleWithData.toCard() = Card(
        name ?: "",
        info ?: "",
        url,
        SOURCE,
        NYT_LOGO_URL
    )
}