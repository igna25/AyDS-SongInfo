package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.lastFM.LastFMArticleService
import ayds.songinfo.moredetails.data.local.lastFM.LastFMLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository

class ArtistBiographyRepositoryImpl(
    private val lastFMLocalStorage: LastFMLocalStorage,
    private val lastFMArticleService: LastFMArticleService
): ArtistBiographyRepository {
    override fun getAristBiographyByArtistName(artistName: String): ArtistBiography {
        var artistBiography = lastFMLocalStorage.getArtistBiographyByArtistName(artistName)

        if (artistBiography != null){
            artistBiography.markItAsLocal()
        }
        else {
            artistBiography = lastFMArticleService.getArtistBiography(artistName)
            if (artistBiography != null){
                if (artistBiography.biography.isNotEmpty()) {
                    lastFMLocalStorage.insertArtistBiography(artistBiography)
                }
            }
            else {
                artistBiography = ArtistBiography(artistName, "", "")
            }
        }

        return artistBiography
    }

    private fun ArtistBiography.markItAsLocal() {
        isLocallyStored = true
    }
}