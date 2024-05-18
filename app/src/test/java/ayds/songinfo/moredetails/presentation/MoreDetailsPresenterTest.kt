package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
class MoreDetailsPresenterTest {
    private val repository: ArtistBiographyRepository = mockk()
    private val presenter: MoreDetailsPresenter = MoreDetailsPresenterImpl(repository)

    @Test
    fun `on search artist biography should notify the result`() {
        val artistBiography: ArtistBiography = mockk()
        every { repository.getAristBiographyByArtistName("name") } returns artistBiography
        val artistBiographyTester: (ArtistBiography) -> Unit = mockk(relaxed = true)
        presenter.artistBiographyObservable.subscribe { artistBiographyTester(it) }

        presenter.searchArtistBiography("name")

        verify { artistBiographyTester(artistBiography) }
    }
}