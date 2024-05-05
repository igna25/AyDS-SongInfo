package ayds.songinfo.moredetails.data.local.lastFM

import ayds.songinfo.moredetails.domain.ArtistBiography

interface LastFMLocalStorage {
    fun getArtistBiographyByArtistName(artistName: String): ArtistBiography?

    fun insertArtistBiography(artistBiography: ArtistBiography)
}