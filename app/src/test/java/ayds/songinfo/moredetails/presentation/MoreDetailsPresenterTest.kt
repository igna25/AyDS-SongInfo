package ayds.songinfo.moredetails.presentation

import ayds.artist.external.lastfm.data.ArtistBiography
import ayds.songinfo.moredetails.domain.MoreDetailsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
class MoreDetailsPresenterTest {
    private val repository: MoreDetailsRepository = mockk()
    private val presenter: MoreDetailsPresenter = MoreDetailsPresenterImpl(repository)

    @Test
    fun `on search artist biography should notify the result`() {
        val artistBiography: ArtistBiography = mockk()
        every { repository.getDetailsByArtistName("name") } returns artistBiography
        val artistBiographyTester: (ArtistBiography) -> Unit = mockk(relaxed = true)
        presenter.artistBiographyObservable.subscribe { artistBiographyTester(it) }

        presenter.searchArtistDetails("name")

        verify { artistBiographyTester(artistBiography) }
    }
}