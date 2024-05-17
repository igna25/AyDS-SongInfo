package ayds.songinfo.moredetails.injector

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
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelper
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsViewActivity
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object MoreDetailsInjector {
    private const val ARTICLE_DATABASE_NAME = "database-name-thename"
    private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

    lateinit var moreDetailsPresenter: MoreDetailsPresenter
    lateinit var artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper

    private lateinit var artistBiographyRepository: ArtistBiographyRepository

    private lateinit var lastFMLocalStorage: LastFMLocalStorage
    private lateinit var lastFMArticleService: LastFMArticleService

    private lateinit var lastFMToArtistBiographyResolver: LastFMToArtistBiographyResolver

    private lateinit var articleDatabase: ArticleDatabase
    private lateinit var lastFMArticleAPI: LastFMArticleAPI

    fun init(moreDetailsViewActivity: MoreDetailsViewActivity){
        initArticleDatabase(moreDetailsViewActivity)
        initLastFMArticleAPI()

        initLastFMToArtistBiographyResolver()

        initLastFMLocalStorage()
        initLastFMArticleService()

        initArtistBiographyRepository()

        initMoreDetailsPresenter()
        initArtistBiographyDescriptionHelper()
    }

    private fun initArticleDatabase(moreDetailsViewActivity: MoreDetailsViewActivity) {
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

    private fun initArtistBiographyDescriptionHelper() {
        artistBiographyDescriptionHelper = ArtistBiographyDescriptionHelperImpl()
    }
}