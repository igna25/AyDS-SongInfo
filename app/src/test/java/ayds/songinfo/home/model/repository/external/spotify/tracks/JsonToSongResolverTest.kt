package ayds.songinfo.home.model.repository.external.spotify.tracks

import org.junit.Assert.assertEquals
import org.junit.Test

class JsonToSongResolverTest {

    private val resolver = JsonToSongResolver()

    @Test
    fun `given service data with preview url should return song with preview url`() {
        val serviceData = """
            {
                "tracks": {
                    "items": [
                        {
                            "id": "1",
                            "name": "Song Name",
                            "artists": [{"name": "Artist Name"}],
                            "album": {
                                "name": "Album Name",
                                "images": [{}, {"url": "image_url"}],
                                "release_date": "2023-01-01",
                                "release_date_precision": "day"
                            },
                            "external_urls": {"spotify": "spotify_url"},
                            "preview_url": "preview_url"
                        }
                    ]
                }
            }
        """.trimIndent()

        val result = resolver.getSongFromExternalData(serviceData)

        assertEquals("preview_url", result?.previewUrl)
    }

    @Test
    fun `given service data without preview url should return song with empty preview url`() {
        val serviceData = """
            {
                "tracks": {
                    "items": [
                        {
                            "id": "1",
                            "name": "Song Name",
                            "artists": [{"name": "Artist Name"}],
                            "album": {
                                "name": "Album Name",
                                "images": [{}, {"url": "image_url"}],
                                "release_date": "2023-01-01",
                                "release_date_precision": "day"
                            },
                            "external_urls": {"spotify": "spotify_url"},
                            "preview_url": null
                        }
                    ]
                }
            }
        """.trimIndent()

        val result = resolver.getSongFromExternalData(serviceData)

        assertEquals("", result?.previewUrl)
    }
}
