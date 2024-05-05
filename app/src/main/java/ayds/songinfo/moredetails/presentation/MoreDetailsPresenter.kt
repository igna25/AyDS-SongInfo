package ayds.songinfo.moredetails.presentation

import android.app.Activity
import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository
import ayds.songinfo.utils.UtilsInjector.navigationUtils

interface MoreDetailsPresenter {
    val artistBiographyObservable: Observable<ArtistBiography>

    fun searchArtistBiography(artistName: String)

    fun openExternalLink(activity: Activity, url: String)
}

internal class MoreDetailsPresenterImpl(private val repository: ArtistBiographyRepository): MoreDetailsPresenter {
    override val artistBiographyObservable = Subject<ArtistBiography>()

    override fun searchArtistBiography(artistName: String) {
        repository.getAristBiographyByArtistName(artistName).let {
            artistBiographyObservable.notify(it)
        }
    }

    override fun openExternalLink(activity: Activity, url: String) {
        navigationUtils.openExternalUrl(activity, url)
    }
}