package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.MoreDetailsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
class MoreDetailsPresenterTest {
    private val repository: MoreDetailsRepository = mockk()
    private val descriptionHelper: DescriptionHelper = mockk()
    private val presenter: MoreDetailsPresenter = MoreDetailsPresenterImpl(repository, descriptionHelper)

    @Test
    fun `on search artist details should notify the result`() {
        val card = Card("artistName", "description", "infoUrl", "source", "sourceUrl")
        val cards: List<Card> = listOf(card)
        val cardUi = CardUiState("artistName", "infoHTML", "infoUrl", "source", "sourceUrl")
        val cardsUi: List<CardUiState> = listOf(cardUi)
        every { descriptionHelper.getDescription(card) } returns "infoHTML"
        every { repository.getDetailsByArtistName("name") } returns cards
        val cardsTester: (List<CardUiState>) -> Unit = mockk(relaxed = true)
        presenter.cardsObservable.subscribe { cardsTester(it) }

        presenter.searchArtistDetails("name")

        verify { cardsTester(cardsUi) }
    }
}