package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository
import ayds.songinfo.moredetails.domain.Card

interface MoreDetailsPresenter {
    val artistBiographyObservable: Observable<CardUiState>


    fun searchArtistBiography(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: ArtistBiographyRepository,
    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper
    ): MoreDetailsPresenter {
    override val artistBiographyObservable = Subject<CardUiState>()


    override fun searchArtistBiography(artistName: String) {
        repository.getAristBiographyByArtistName(artistName).let {
            artistBiographyObservable.notify(it.toUiState())
        }
    }

    private fun Card.toUiState() = CardUiState(
        artistName,
        artistBiographyDescriptionHelper.getDescription(this),
        infoUrl,
        source,
        sourceLogoUrl,
    )
}