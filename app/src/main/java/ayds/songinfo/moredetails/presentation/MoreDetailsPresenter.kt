package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.MoreDetailsRepository
import ayds.songinfo.moredetails.domain.Card

interface MoreDetailsPresenter {
    val artistBiographyObservable: Observable<List<CardUiState>>


    fun searchArtistDetails(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: MoreDetailsRepository,
    private val descriptionHelper: DescriptionHelper
    ): MoreDetailsPresenter {
    override val artistBiographyObservable = Subject<List<CardUiState>>()


    override fun searchArtistDetails(artistName: String) {
        repository.getDetailsByArtistName(artistName).let {
            artistBiographyObservable.notify(it.map { card -> card.toUiState() })
        }
    }

    private fun Card.toUiState() = CardUiState(
        artistName,
        descriptionHelper.getDescription(this),
        infoUrl,
        source,
        sourceLogoUrl,
    )
}