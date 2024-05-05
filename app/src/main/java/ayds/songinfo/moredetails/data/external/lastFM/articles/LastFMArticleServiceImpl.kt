package ayds.songinfo.moredetails.data.external.lastFM.articles

import ayds.songinfo.moredetails.data.external.lastFM.LastFMArticleService
import ayds.songinfo.moredetails.domain.ArtistBiography
import retrofit2.Response

internal class LastFMArticleServiceImpl(
    private val lastFMArticleAPI: LastFMArticleAPI,
    private val lastFMToArtistBiographyResolver: LastFMToArtistBiographyResolver
): LastFMArticleService {

    override fun getArtistBiography(artistName: String): ArtistBiography? {
        val callResponse = getArtistBiographyCallResponseFromService(artistName)
        return lastFMToArtistBiographyResolver.getArtistBiographyFromExternalData(callResponse.body())
    }

    private fun getArtistBiographyCallResponseFromService(artistName: String): Response<String> =
        lastFMArticleAPI.getArtistInfo(artistName).execute()
}