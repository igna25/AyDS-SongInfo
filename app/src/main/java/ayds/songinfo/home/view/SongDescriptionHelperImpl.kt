package ayds.songinfo.home.view

import android.icu.text.SimpleDateFormat
import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong
import java.time.Year

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release date: ${releaseDateString(song)}"
            else -> "Song not found"
        }
    }

    fun releaseDateString(song: SpotifySong): String {
        return when (song.releaseDatePrecision) {
            "day" -> SimpleDateFormat("dd/MM/yyyy").format(SimpleDateFormat("yyyy-MM-dd").parse(song.releaseDate))
            "month" -> SimpleDateFormat("MMMM, yyyy").format(SimpleDateFormat("yyyy-MM").parse(song.releaseDate))
            "year" -> toYearLeapOrNotFormat(song.releaseDate.split("-").first().toLong())
            else -> "Invalid released date precision"
        }
    }

    fun toYearLeapOrNotFormat(year: Long): String {
        return "$year (${if (Year.isLeap(year)) "" else "Not a "}leap year)"
    }
}