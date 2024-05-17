package ayds.songinfo.moredetails.data.external.lastFM.articles

import ayds.songinfo.moredetails.domain.ArtistBiography
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

internal class JsonToArtistBiographyResolver : LastFMToArtistBiographyResolver {
    override fun getArtistBiographyFromExternalData(serviceData: String?): ArtistBiography? =
        try {
            serviceData?.getArtist()?.let { artist ->
                ArtistBiography(
                    artist.getName(),
                    artist.getBiography(),
                    artist.getArticleUrl()
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