package ayds.songinfo.moredetails.data.local.lastFM

import ayds.artist.external.lastfm.data.ArtistBiography

interface LastFMLocalStorage {
    fun getArtistBiographyByArtistName(artistName: String): ArtistBiography?

    fun insertArtistBiography(artistBiography: ArtistBiography)
}