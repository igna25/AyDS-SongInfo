package ayds.artist.external.lastfm.injector

import ayds.artist.external.lastfm.data.JsonToArtistBiographyResolver
import ayds.artist.external.lastfm.data.LastFMArticleService
import ayds.artist.external.lastfm.data.LastFMArticleServiceImpl
import ayds.artist.external.lastfm.data.LastFMToArtistBiographyResolver
import ayds.artist.external.lastfm.data.LastFMArticleAPI
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
object LastFMInjector {
    private val lastFMArticleAPI = getLastFMAPI()
    private val lastFMToArtistBiographyResolver: LastFMToArtistBiographyResolver = JsonToArtistBiographyResolver()

    val lastFMArticleService: LastFMArticleService = LastFMArticleServiceImpl(lastFMArticleAPI, lastFMToArtistBiographyResolver)

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(LASTFM_BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private fun getLastFMAPI() = getRetrofit().create(LastFMArticleAPI::class.java)



}