package ayds.songinfo.moredetails.injector

import androidx.room.Room
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.data.Broker
import ayds.songinfo.moredetails.data.BrokerImpl
import ayds.songinfo.moredetails.data.MoreDetailsRepositoryImpl
import ayds.songinfo.moredetails.data.local.lastFM.MoreDetailsLocalStorage
import ayds.songinfo.moredetails.data.local.lastFM.room.MoreDetailsDatabase
import ayds.songinfo.moredetails.data.local.lastFM.room.MoreDetailsLocalStorageRoomImpl
import ayds.songinfo.moredetails.data.proxy.LastFMProxy
import ayds.songinfo.moredetails.data.proxy.NewYorkTimesProxy
import ayds.songinfo.moredetails.data.proxy.WikipediaProxy
import ayds.songinfo.moredetails.domain.MoreDetailsRepository
import ayds.songinfo.moredetails.presentation.DescriptionHelper
import ayds.songinfo.moredetails.presentation.DescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsViewActivity

object MoreDetailsInjector {
    private const val MORE_DETAILS_DATABASE_NAME = "more-details-database"


    lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var descriptionHelper: DescriptionHelper

    private lateinit var moreDetailsRepository: MoreDetailsRepository

    private lateinit var broker: Broker

    private lateinit var lastFMProxy: LastFMProxy
    private lateinit var wikipediaProxy: WikipediaProxy
    private lateinit var newYorkTimesProxy: NewYorkTimesProxy

    private lateinit var moreDetailsLocalStorage: MoreDetailsLocalStorage

    private lateinit var moreDetailsDatabase: MoreDetailsDatabase

    fun init(moreDetailsViewActivity: MoreDetailsViewActivity){
        initMoreDetailsDatabase(moreDetailsViewActivity)

        initLastFMLocalStorage()

        initLastFMProxy()
        initWikipediaProxy()
        initNewYorkTimesProxy()

        initBroker()

        initArtistBiographyRepository()

        initArtistBiographyDescriptionHelper()
        initMoreDetailsPresenter()
    }

    private fun initMoreDetailsDatabase(moreDetailsViewActivity: MoreDetailsViewActivity) {
        moreDetailsDatabase =
            Room.databaseBuilder(moreDetailsViewActivity, MoreDetailsDatabase::class.java, MORE_DETAILS_DATABASE_NAME).build()
    }

    private fun initLastFMLocalStorage() {
        moreDetailsLocalStorage = MoreDetailsLocalStorageRoomImpl(moreDetailsDatabase)
    }

    private fun initLastFMProxy() {
        lastFMProxy = LastFMProxy(LastFMInjector.lastFMArticleService)
    }

    private fun initWikipediaProxy() {
        wikipediaProxy = WikipediaProxy(WikipediaInjector.wikipediaTrackService)
    }

    private fun initNewYorkTimesProxy() {
        newYorkTimesProxy = NewYorkTimesProxy(NYTimesInjector.nyTimesService)
    }

    private fun initBroker() {
        broker = BrokerImpl(listOf(lastFMProxy, wikipediaProxy, newYorkTimesProxy))
    }

    private fun initArtistBiographyRepository() {
        moreDetailsRepository = MoreDetailsRepositoryImpl(moreDetailsLocalStorage, broker)
    }

    private fun initMoreDetailsPresenter() {
        moreDetailsPresenter = MoreDetailsPresenterImpl(moreDetailsRepository, descriptionHelper)
    }

    private fun initArtistBiographyDescriptionHelper() {
        descriptionHelper = DescriptionHelperImpl()
    }
}