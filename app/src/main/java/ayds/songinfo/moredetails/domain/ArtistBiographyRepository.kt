package ayds.songinfo.moredetails.domain

import ayds.artist.external.lastfm.data.ArtistBiography

interface ArtistBiographyRepository {
    fun getAristBiographyByArtistName(artistName: String): ArtistBiography
}