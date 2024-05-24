package ayds.songinfo.moredetails.domain

interface MoreDetailsRepository {
    fun getDetailsByArtistName(artistName: String): List<Card>
}