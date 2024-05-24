package ayds.songinfo.moredetails.domain

interface ArtistBiographyRepository {
    fun getAristBiographyByArtistName(artistName: String): Card
}