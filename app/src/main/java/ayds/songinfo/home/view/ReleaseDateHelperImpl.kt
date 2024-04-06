package ayds.songinfo.home.view

import android.icu.text.SimpleDateFormat
import ayds.songinfo.home.model.entities.Song.SpotifySong
import java.time.Year

interface ReleaseDateHelper {
    fun getReleaseDateString(song: SpotifySong) : String
}

internal class ReleaseDateHelperImpl : ReleaseDateHelper {
    override fun getReleaseDateString(song: SpotifySong): String {
        return when (song.releaseDatePrecision) {
            "day" -> getDayString(song.releaseDate)
            "month" -> getMonthString(song.releaseDate)
            "year" -> getYearString(song.releaseDate)
            else -> getInvalidString()
        }
    }

    private fun getDayString(releaseDate: String): String {
        return SimpleDateFormat("dd/MM/yyyy").format(SimpleDateFormat("yyyy-MM-dd").parse(releaseDate))
    }

    private fun getMonthString(releaseDate: String): String {
        return SimpleDateFormat("MMMM, yyyy").format(SimpleDateFormat("yyyy-MM").parse(releaseDate))
    }

    private fun getYearString(releaseDate: String): String {
        return toYearLeapOrNotFormat(releaseDate.split("-").first().toLong())
    }

    private fun toYearLeapOrNotFormat(year: Long): String {
        return "$year (${if (Year.isLeap(year)) "" else "Not a "}leap year)"
    }

    private fun getInvalidString(): String {
        return "Invalid released date precision"
    }
}