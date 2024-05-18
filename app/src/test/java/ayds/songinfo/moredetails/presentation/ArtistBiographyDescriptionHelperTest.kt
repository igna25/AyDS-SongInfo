package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiography
import org.junit.Test
import org.junit.Assert.assertEquals

class ArtistBiographyDescriptionHelperTest {
    private val artistBiographyDescriptionHelper = ArtistBiographyDescriptionHelperImpl()

    @Test
    fun `given a local non empty biography should return the article`() {
        val artistBiography = ArtistBiography(
            "name",
            "bio",
            "url",
            true
        )

        val result = artistBiographyDescriptionHelper.getDescription(artistBiography)

        val expected =
            "<html><div width=400><font face=\"arial\">[*]bio</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `given a local empty biography should return No Results`() {
        val artistBiography = ArtistBiography(
            "name",
            "",
            "url",
            true
        )

        val result = artistBiographyDescriptionHelper.getDescription(artistBiography)

        val expected =
            "<html><div width=400><font face=\"arial\">[*]No Results</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non local non empty biography should return the article`() {
        val artistBiography = ArtistBiography(
            "name",
            "bio",
            "url",
            false
        )

        val result = artistBiographyDescriptionHelper.getDescription(artistBiography)

        val expected =
            "<html><div width=400><font face=\"arial\">bio</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non local empty biography should return No Results`() {
        val artistBiography = ArtistBiography(
            "name",
            "",
            "url",
            false
        )

        val result = artistBiographyDescriptionHelper.getDescription(artistBiography)

        val expected =
            "<html><div width=400><font face=\"arial\">No Results</font></div></html>"

        assertEquals(expected, result)
    }
}