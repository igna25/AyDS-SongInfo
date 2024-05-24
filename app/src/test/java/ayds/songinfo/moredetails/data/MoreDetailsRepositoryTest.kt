package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.local.lastFM.DoreDetailsLocalStorage
import ayds.artist.external.lastfm.data.ArtistBiography
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MoreDetailsRepositoryTest {
    private val lastFMLocalStorage: DoreDetailsLocalStorage = mockk(relaxUnitFun = true)
    private val lastFMArticleService: ayds.artist.external.lastfm.data.LastFMArticleService = mockk(relaxUnitFun = true)

    private val repository = MoreDetailsRepositoryImpl(lastFMLocalStorage, lastFMArticleService)

    @Test
    fun `given local artist biography should return the artist biography and mark it as local`() {
        val artistBiography = ArtistBiography(
            "name",
            "bio",
            "url",
            false
        )
        every { lastFMLocalStorage.getArtistBiographyByArtistName("name") } returns artistBiography

        val result = repository.getDetailsByArtistName("name")

        assertEquals(artistBiography, result)
        assertTrue(result.isLocallyStored)
    }

    @Test
    fun `given null artist biography should return empty artist biography`() {
        every { lastFMLocalStorage.getArtistBiographyByArtistName("name") } returns null
        every { lastFMArticleService.getArtistBiography("name") } returns null

        val result = repository.getDetailsByArtistName("name")

        val expected = ArtistBiography("name", "", "")

        assertEquals(expected, result)
        assertFalse(result.isLocallyStored)
    }

    @Test
    fun `given non local non empty artist biography should return the artist biography and store it`() {
        val artistBiography = ArtistBiography(
            "name",
            "bio",
            "url",
            false
        )
        every { lastFMLocalStorage.getArtistBiographyByArtistName("name") } returns null
        every { lastFMArticleService.getArtistBiography("name") } returns artistBiography

        val result = repository.getDetailsByArtistName("name")

        assertEquals(artistBiography, result)
        assertFalse(result.isLocallyStored)
        verify { lastFMLocalStorage.insertArtistBiography(artistBiography) }
    }

    @Test
    fun `given non local empty artist biography should return the artist biography`() {
        val artistBiography = ArtistBiography(
            "name",
            "",
            "url",
            false
        )
        every { lastFMLocalStorage.getArtistBiographyByArtistName("name") } returns null
        every { lastFMArticleService.getArtistBiography("name") } returns artistBiography

        val result = repository.getDetailsByArtistName("name")

        assertEquals(artistBiography, result)
        assertFalse(result.isLocallyStored)
    }
}