package ayds.songinfo.moredetails.injector

import androidx.room.Room
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.songinfo.moredetails.data.ArtistBiographyRepositoryImpl
import ayds.songinfo.moredetails.data.local.lastFM.LastFMLocalStorage
import ayds.songinfo.moredetails.data.local.lastFM.room.ArticleDatabase
import ayds.songinfo.moredetails.data.local.lastFM.room.LastFMLocalStorageRoomImpl
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelper
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsViewActivity

object MoreDetailsInjector {
    private const val ARTICLE_DATABASE_NAME = "database-name-thename"


    lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper

    private lateinit var artistBiographyRepository: ArtistBiographyRepository

    private lateinit var lastFMLocalStorage: LastFMLocalStorage

    private lateinit var articleDatabase: ArticleDatabase

    fun init(moreDetailsViewActivity: MoreDetailsViewActivity){
        initArticleDatabase(moreDetailsViewActivity)

        initLastFMLocalStorage()

        initArtistBiographyRepository()

        initArtistBiographyDescriptionHelper()
        initMoreDetailsPresenter()
    }

    private fun initArticleDatabase(moreDetailsViewActivity: MoreDetailsViewActivity) {
        articleDatabase =
            Room.databaseBuilder(moreDetailsViewActivity, ArticleDatabase::class.java, ARTICLE_DATABASE_NAME).build()
    }

    private fun initLastFMLocalStorage() {
        lastFMLocalStorage = LastFMLocalStorageRoomImpl(articleDatabase)
    }

    private fun initArtistBiographyRepository() {
        artistBiographyRepository = ArtistBiographyRepositoryImpl(lastFMLocalStorage, LastFMInjector.lastFMArticleService)
    }

    private fun initMoreDetailsPresenter() {
        moreDetailsPresenter = MoreDetailsPresenterImpl(artistBiographyRepository, artistBiographyDescriptionHelper)
    }

    private fun initArtistBiographyDescriptionHelper() {
        artistBiographyDescriptionHelper = ArtistBiographyDescriptionHelperImpl()
    }
}