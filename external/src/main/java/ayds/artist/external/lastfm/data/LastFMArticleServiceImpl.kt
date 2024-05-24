package ayds.artist.external.lastfm.data

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