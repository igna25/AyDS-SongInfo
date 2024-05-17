package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository

interface MoreDetailsPresenter {
    val artistBiographyObservable: Observable<ArtistBiography>

    fun searchArtistBiography(artistName: String)
}

internal class MoreDetailsPresenterImpl(private val repository: ArtistBiographyRepository): MoreDetailsPresenter {
    override val artistBiographyObservable = Subject<ArtistBiography>()

    override fun searchArtistBiography(artistName: String) {
        repository.getAristBiographyByArtistName(artistName).let {
            artistBiographyObservable.notify(it)
        }
    }
}