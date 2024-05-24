package ayds.songinfo.moredetails.domain

data class Card(
    val artistName: String,
    val description: String,
    val infoUrl: String,
    val source: String,
    val sourceLogoUrl: String,
    var isLocallyStored: Boolean = false,
)
