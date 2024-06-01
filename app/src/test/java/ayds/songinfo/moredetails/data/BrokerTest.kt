package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.proxy.Proxy
import ayds.songinfo.moredetails.domain.Card
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals


class BrokerTest{
    private val proxy1: Proxy = mockk()
    private val proxy2: Proxy = mockk()
    private val broker = BrokerImpl(listOf(proxy1, proxy2))

    @Test
    fun `given null cards should return an empty list`(){
        every { proxy1.getCard("name") } returns null
        every { proxy2.getCard("name") } returns null

        val result = broker.getCards("name")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given a non null card should return that card`(){
        val card = Card(
            "name",
            "bio",
            "url",
            "source",
            "sourceUrl",
            false,
        )
        val expected = listOf(card)
        every { proxy1.getCard("name") } returns card
        every { proxy2.getCard("name") } returns null

        val result = broker.getCards("name")

        assertEquals(expected, result)
    }

    @Test
    fun `given two non null cards should return those cards`(){
        val card = Card(
            "name",
            "bio",
            "url",
            "source",
            "sourceUrl",
            false,
        )
        val secondCard = Card(
            "name",
            "bio",
            "url",
            "source2",
            "sourceUrl",
            false,
        )
        val expected = listOf(card, secondCard)
        every { proxy1.getCard("name") } returns card
        every { proxy2.getCard("name") } returns secondCard

        val result = broker.getCards("name")

        assertEquals(expected, result)
    }

}