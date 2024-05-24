package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.artist.external.lastfm.data.ArtistBiography
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository

interface MoreDetailsPresenter {
    val artistBiographyObservable: Observable<ArtistBiographyUiState>


    fun searchArtistBiography(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: ArtistBiographyRepository,
    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper
    ): MoreDetailsPresenter {
    override val artistBiographyObservable = Subject<ArtistBiographyUiState>()


    override fun searchArtistBiography(artistName: String) {
        repository.getAristBiographyByArtistName(artistName).let {
            artistBiographyObservable.notify(it.toUiState())
        }
    }

    private fun ArtistBiography.toUiState() = ArtistBiographyUiState(
        artistName,
        artistBiographyDescriptionHelper.getDescription(this),
        articleUrl
    )
}