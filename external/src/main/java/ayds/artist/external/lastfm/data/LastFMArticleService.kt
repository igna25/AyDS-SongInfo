package ayds.artist.external.lastfm.data

interface LastFMArticleService {
    fun getArtistBiography(artistName: String): ArtistBiography?
}