package ayds.songinfo.moredetails

import androidx.room.Room
import ayds.songinfo.moredetails.data.ArtistBiographyRepositoryImpl
import ayds.songinfo.moredetails.data.external.lastFM.LastFMArticleService
import ayds.songinfo.moredetails.data.external.lastFM.articles.JsonToArtistBiographyResolver
import ayds.songinfo.moredetails.data.external.lastFM.articles.LastFMArticleAPI
import ayds.songinfo.moredetails.data.external.lastFM.articles.LastFMArticleServiceImpl
import ayds.songinfo.moredetails.data.external.lastFM.articles.LastFMToArtistBiographyResolver
import ayds.songinfo.moredetails.data.local.lastFM.LastFMLocalStorage
import ayds.songinfo.moredetails.data.local.lastFM.room.ArticleDatabase
import ayds.songinfo.moredetails.data.local.lastFM.room.LastFMLocalStorageRoomImpl
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsViewActivity
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object MoreDetailsInjector {
    private const val ARTICLE_DATABASE_NAME = "database-name-thename"
    private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

    private lateinit var moreDetailsViewActivity: MoreDetailsViewActivity
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter

    private lateinit var artistBiographyRepository: ArtistBiographyRepository

    private lateinit var lastFMLocalStorage: LastFMLocalStorage
    private lateinit var lastFMArticleService: LastFMArticleService

    private lateinit var lastFMToArtistBiographyResolver: LastFMToArtistBiographyResolver

    private lateinit var articleDatabase: ArticleDatabase
    private lateinit var lastFMArticleAPI: LastFMArticleAPI

    fun getMoreDetailsPresenter() = moreDetailsPresenter

    fun init(moreDetailsViewActivity: MoreDetailsViewActivity){
        initMoreDetailsView(moreDetailsViewActivity)

        initArticleDatabase()
        initLastFMArticleAPI()

        initLastFMToArtistBiographyResolver()

        initLastFMLocalStorage()
        initLastFMArticleService()

        initArtistBiographyRepository()

        initMoreDetailsPresenter()
    }

    private fun initMoreDetailsView(moreDetailsViewActivity: MoreDetailsViewActivity){
        this.moreDetailsViewActivity = moreDetailsViewActivity
    }

    private fun initArticleDatabase() {
        articleDatabase =
            Room.databaseBuilder(moreDetailsViewActivity, ArticleDatabase::class.java, ARTICLE_DATABASE_NAME).build()
    }

    private fun initLastFMArticleAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        lastFMArticleAPI = retrofit.create(LastFMArticleAPI::class.java)
    }

    private fun initLastFMToArtistBiographyResolver() {
        lastFMToArtistBiographyResolver = JsonToArtistBiographyResolver()
    }

    private fun initLastFMLocalStorage() {
        lastFMLocalStorage = LastFMLocalStorageRoomImpl(articleDatabase)
    }

    private fun initLastFMArticleService() {
        lastFMArticleService = LastFMArticleServiceImpl(lastFMArticleAPI, lastFMToArtistBiographyResolver)
    }

    private fun initArtistBiographyRepository() {
        artistBiographyRepository = ArtistBiographyRepositoryImpl(lastFMLocalStorage, lastFMArticleService)
    }

    private fun initMoreDetailsPresenter() {
        moreDetailsPresenter = MoreDetailsPresenterImpl(artistBiographyRepository)
    }
}