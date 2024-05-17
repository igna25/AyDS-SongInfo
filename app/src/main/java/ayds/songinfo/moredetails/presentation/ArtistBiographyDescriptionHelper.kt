package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiography
import java.util.Locale

interface ArtistBiographyDescriptionHelper {
    fun getDescription(artistBiography: ArtistBiography): String
}

internal class ArtistBiographyDescriptionHelperImpl : ArtistBiographyDescriptionHelper {
    override fun getDescription(artistBiography: ArtistBiography): String {
        return textToHtml(getTextBiography(artistBiography), artistBiography.artistName)
    }

    private fun getTextBiography(artistBiography: ArtistBiography): String {
        val prefix = if (artistBiography.isLocallyStored) "[*]" else ""
        val text = if (artistBiography.biography.isEmpty()) NO_RESULTS else artistBiography.biography.replace("\\n", "\n")
        return prefix + text
    }

    companion object {
        private const val HEADER = "<html><div width=400><font face=\"arial\">"
        private const val FOOTER = "</font></div></html>"
        const val NO_RESULTS = "No Results"

        private fun textToHtml(text: String, term: String): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append(HEADER)
            stringBuilder.append(getTextWithBold(text, term))
            stringBuilder.append(FOOTER)
            return stringBuilder.toString()
        }

        private fun getTextWithBold(text: String, term: String): String {
            text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
                )
            return text
        }
    }
}