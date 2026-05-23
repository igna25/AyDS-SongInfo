package ayds.artist.external.newyorktimes.data

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Response

private const val PROP_RESPONSE = "response"
private const val WEB_URL = "web_url"
private const val DOCS = "docs"

interface NYTimesToArtistResolver {
    fun getURL(response: Response<String>): String
    fun generateFormattedResponse(response: Response<String>, nameArtist: String?): String?
}

class NYTimesToArtistResolverImpl : NYTimesToArtistResolver {

    override fun getURL(response: Response<String>): String {
        return try {
            val jsonResponse = generateResponse(response)
            jsonResponse?.get(DOCS)?.asJsonArray?.get(0)?.asJsonObject?.get(WEB_URL)?.asString ?: ""
        } catch (_: Exception) {
            ""
        }
    }

    override fun generateFormattedResponse(response: Response<String>, nameArtist: String?): String? {
        return try {
            val jsonResponse = generateResponse(response)
            val abstract = jsonResponse?.let { getAsJsonObject(it) }
            abstract?.let { artistInfoAbstractToString(it) }
        } catch (_: Exception) {
            null
        }
    }

    private fun getJson(callResponse: Response<String>): JsonObject? {
        val gson = Gson()
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }

    private fun generateResponse(response: Response<String>): JsonObject? {
        val jObj = getJson(response)
        return jObj?.get(PROP_RESPONSE)?.asJsonObject
    }

    private fun getAsJsonObject(response: JsonObject): JsonElement? {
        return response[DOCS].asJsonArray[0].asJsonObject["abstract"]
    }

    private fun artistInfoAbstractToString(abstract: JsonElement): String {
        return abstract.asString.replace("\\n", "\n")
    }

}