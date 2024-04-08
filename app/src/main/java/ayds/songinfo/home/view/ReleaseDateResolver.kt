package ayds.songinfo.home.view

import android.icu.text.SimpleDateFormat
import ayds.songinfo.home.model.entities.Song.SpotifySong
import java.time.Year

interface ReleaseDateResolverFactory {
    fun get(song: SpotifySong): ReleaseDateResolver
}

object ReleaseDateResolverFactoryImpl : ReleaseDateResolverFactory {
    override fun get(song: SpotifySong): ReleaseDateResolver {
        return when (song.releaseDatePrecision) {
            "day" -> ReleaseDateDayResolver(song.releaseDate)
            "month" -> ReleaseDateMonthResolver(song.releaseDate)
            "year" -> ReleaseDateYearResolver(song.releaseDate)
            else -> ReleaseDateDefaultResolver(song.releaseDate)
        }
    }
}

interface ReleaseDateResolver {
    fun getReleaseDateString(): String
}

internal class ReleaseDateDayResolver(private val releaseDate: String) : ReleaseDateResolver {
    override fun getReleaseDateString(): String {
        return SimpleDateFormat("dd/MM/yyyy").format(SimpleDateFormat("yyyy-MM-dd").parse(releaseDate))
    }
}

internal class ReleaseDateMonthResolver(private val releaseDate: String) : ReleaseDateResolver {
    override fun getReleaseDateString(): String {
        return SimpleDateFormat("MMMM, yyyy").format(SimpleDateFormat("yyyy-MM").parse(releaseDate))
    }
}

internal class ReleaseDateYearResolver(private val releaseDate: String) : ReleaseDateResolver {
    override fun getReleaseDateString(): String {
        return toYearLeapOrNotFormat(releaseDate.split("-").first().toLong())
    }

    private fun toYearLeapOrNotFormat(year: Long): String {
        return "$year (${if (Year.isLeap(year)) "" else "Not a "}leap year)"
    }
}

internal class ReleaseDateDefaultResolver(private val releaseDate: String) : ReleaseDateResolver {
    override fun getReleaseDateString(): String {
        return releaseDate
    }
}