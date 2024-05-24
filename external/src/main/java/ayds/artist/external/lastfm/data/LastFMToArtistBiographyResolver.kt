package ayds.artist.external.lastfm.data

import com.google.gson.Gson
import com.google.gson.JsonObject

interface LastFMToArtistBiographyResolver {
    fun getArtistBiographyFromExternalData(serviceData: String?): ArtistBiography?
}

private const val ARTIST = "artist"
private const val NAME = "name"
private const val BIOGRAPHY = "bio"
private const val CONTENT = "content"
private const val URL = "url"
private const val LASTFM_LOGO_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

internal class JsonToArtistBiographyResolver : LastFMToArtistBiographyResolver {
    override fun getArtistBiographyFromExternalData(serviceData: String?): ArtistBiography? =
        try {
            serviceData?.getArtist()?.let { artist ->
                ArtistBiography(
                    artist.getName(),
                    artist.getBiography(),
                    artist.getArticleUrl(),
                    LASTFM_LOGO_URL
                )
            }
        } catch (exception: Exception) {
            null
        }

    private fun String?.getArtist(): JsonObject {
        val jsonObject = Gson().fromJson(this, JsonObject::class.java)
        return jsonObject[ARTIST].asJsonObject
    }

    private fun JsonObject.getName() = this[NAME].asString

    private fun JsonObject.getBiography(): String {
        return this[BIOGRAPHY].asJsonObject[CONTENT].asString
    }

    private fun JsonObject.getArticleUrl() = this[URL].asString
}