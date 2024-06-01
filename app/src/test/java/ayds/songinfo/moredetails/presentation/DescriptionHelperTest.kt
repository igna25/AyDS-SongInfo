package ayds.songinfo.moredetails.presentation


import ayds.songinfo.moredetails.domain.Card
import org.junit.Test
import org.junit.Assert.assertEquals

class DescriptionHelperTest {
    private val descriptionHelper = DescriptionHelperImpl()

    @Test
    fun `given a local non empty card should return the article`() {
        val card = Card(
            "name",
            "bio",
            "url",
            "source",
            "sourceUrl",
            true,
        )

        val result = descriptionHelper.getDescription(card)

        val expected =
            "<html><div width=400><font face=\"arial\">[*]bio</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `given a local empty card should return No Results`() {
        val card = Card(
            "name",
            "",
            "url",
            "source",
            "sourceUrl",
            true,
        )

        val result = descriptionHelper.getDescription(card)

        val expected =
            "<html><div width=400><font face=\"arial\">[*]No Results</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non local non empty card should return the article`() {
        val card = Card(
            "name",
            "bio",
            "url",
            "source",
            "sourceUrl",
            false,
        )

        val result = descriptionHelper.getDescription(card)

        val expected =
            "<html><div width=400><font face=\"arial\">bio</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non local empty card should return No Results`() {
        val card = Card(
            "name",
            "",
            "url",
            "source",
            "sourceUrl",
            false,
        )

        val result = descriptionHelper.getDescription(card)

        val expected =
            "<html><div width=400><font face=\"arial\">No Results</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `should map slash n to br`() {
        val card = Card(
            "name",
            "a\n",
            "url",
            "source",
            "sourceUrl",
            false,
        )

        val result = descriptionHelper.getDescription(card)

        val expected =
            "<html><div width=400><font face=\"arial\">a<br></font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `should map double slash n to br`() {
        val card = Card(
            "name",
            "a\\n",
            "url",
            "source",
            "sourceUrl",
            false,
        )

        val result = descriptionHelper.getDescription(card)

        val expected =
            "<html><div width=400><font face=\"arial\">a<br></font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `should set artist name uppercase bold`() {
        val card = Card(
            "name",
            "a name",
            "url",
            "source",
            "sourceUrl",
            false,
        )

        val result = descriptionHelper.getDescription(card)

        val expected =
            "<html><div width=400><font face=\"arial\">a <b>NAME</b></font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `should remove apostrophes`() {
        val card = Card(
            "name",
            "a'",
            "url",
            "source",
            "sourceUrl",
            false,
        )

        val result = descriptionHelper.getDescription(card)

        val expected =
            "<html><div width=400><font face=\"arial\">a </font></div></html>"

        assertEquals(expected, result)
    }
}