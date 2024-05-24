package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import java.util.Locale


interface DescriptionHelper {
    fun getDescription(card: Card): String
}

internal class DescriptionHelperImpl : DescriptionHelper {
    override fun getDescription(card: Card): String {
        return textToHtml(getTextBiography(card), card.artistName)
    }

    private fun getTextBiography(card: Card): String {
        val prefix = if (card.isLocallyStored) LOCAL_MARKER else ""
        val text = if (card.description.isEmpty()) NO_RESULTS else card.description.replace("\\n", "\n")
        return prefix + text
    }

    companion object {
        private const val LOCAL_MARKER = "[*]"
        private const val HEADER = "<html><div width=400><font face=\"arial\">"
        private const val FOOTER = "</font></div></html>"
        private const val NO_RESULTS = "No Results"

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