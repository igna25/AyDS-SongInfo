package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.local.lastFM.MoreDetailsLocalStorage
import ayds.songinfo.moredetails.domain.Card
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MoreDetailsRepositoryTest {
    private val lastFMLocalStorage: MoreDetailsLocalStorage = mockk(relaxUnitFun = true)
    private val broker: Broker = mockk(relaxUnitFun = true)

    private val repository = MoreDetailsRepositoryImpl(lastFMLocalStorage, broker)

    @Test
    fun `given local cards should return the cards and mark them as local`() {
        val card = Card(
            "name",
            "bio",
            "url",
            "source",
            "sourceUrl",
            false,
        )
        val cards = listOf(card)
        every { lastFMLocalStorage.getDetailsByArtistName("name") } returns cards

        val result = repository.getDetailsByArtistName("name")

        assertEquals(cards, result)
        assertTrue(result.all { it.isLocallyStored })
    }

    @Test
    fun `given empty card list should return no results card`() {
        every { lastFMLocalStorage.getDetailsByArtistName("name") } returns emptyList()
        every { broker.getCards("name") } returns emptyList()

        val result = repository.getDetailsByArtistName("name")

        val expected = listOf(Card("name", "", "", "", ""))

        assertEquals(expected, result)
        assertFalse(result.all { it.isLocallyStored })
    }

    @Test
    fun `given non local non empty card list should return the card list and store it`() {
        val card = Card(
            "name",
            "bio",
            "url",
            "source",
            "sourceUrl",
            false,
        )
        val cards = listOf(card)
        every { lastFMLocalStorage.getDetailsByArtistName("name") } returns emptyList()
        every { broker.getCards("name") } returns cards

        val result = repository.getDetailsByArtistName("name")

        assertEquals(cards, result)
        assertFalse(result.all { it.isLocallyStored })
        cards.forEach { verify { lastFMLocalStorage.insertArtistDetails(it) } }
    }

    @Test
    fun `given non local empty card list should return the card list and not store it`() {
        val card = Card(
            "name",
            "",
            "url",
            "source",
            "sourceUrl",
            false,
        )
        val cards = listOf(card)
        every { lastFMLocalStorage.getDetailsByArtistName("name") } returns emptyList()
        every { broker.getCards("name") } returns cards

        val result = repository.getDetailsByArtistName("name")

        assertEquals(cards, result)
        assertFalse(result.all { it.isLocallyStored })
        cards.forEach { verify (inverse = true) { lastFMLocalStorage.insertArtistDetails(it) } }
    }
}