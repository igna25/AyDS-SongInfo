package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import androidx.core.text.HtmlCompat
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Locale

data class ArtistBiography(val artistName: String, var biography: String, val articleUrl: String)

class OtherInfoWindow : Activity() {
    private lateinit var articleTextView: TextView
    private lateinit var lastFMLogoImageView: ImageView
    private lateinit var openUrlButton: Button

    private lateinit var articleDatabase: ArticleDatabase
    private lateinit var lastFMAPI: LastFMAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initProperties()
        initDatabase()
        initLastFMAPI()

        getArtistInfoAsync()
    }

    private fun initProperties() {
        articleTextView = findViewById(R.id.textPane1)
        lastFMLogoImageView = findViewById(R.id.imageView1)
        openUrlButton = findViewById(R.id.openUrlButton1)
    }

    private fun initDatabase() {
        articleDatabase =
            databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
    }

    private fun initLastFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        lastFMAPI =  retrofit.create(LastFMAPI::class.java)
    }

    private fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        val artistName = getArtistName()
        val article = getArtistBiographyRepository(artistName)
        updateViewAsync(article)
    }

    private fun getArtistName() = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: ""

    private fun getArtistBiographyRepository(artistName: String): ArtistBiography {
        var artistBiography = getArtistBiographyFromDatabase(artistName)

        if (artistBiography != null){
            markArtistBiographyAsLocal(artistBiography)
        }
        else {
            artistBiography = getArtistBiographyFromService(artistName)
            if (artistBiography != null){
                saveToArticleDatabaseAsync(artistBiography)
            }
            else {
                artistBiography = ArtistBiography(artistName, NO_RESULTS, "")
            }
        }

        return artistBiography
    }

    private fun getArtistBiographyFromDatabase(artistName: String) =
        articleDatabase.ArticleDao().getArticleByArtistName(artistName)?.toArtistBiography()

    private fun ArticleEntity.toArtistBiography() = ArtistBiography(this.artistName, this.biography, this.articleUrl)

    private fun markArtistBiographyAsLocal(artistBiography: ArtistBiography) {
        artistBiography.biography = "[*]${artistBiography.biography}"
    }

    private fun getArtistBiographyFromService(artistName: String): ArtistBiography? {
        val callResponse = getArtistBiographyCallResponseFromService(artistName)
        return resolveToArtistBiography(callResponse.body())
    }

    private fun getArtistBiographyCallResponseFromService(artistName: String): Response<String> =
        lastFMAPI.getArtistInfo(artistName).execute()

    private fun resolveToArtistBiography(serviceData: String?): ArtistBiography? =
        try {
            serviceData?.getArtist()?.let { artist ->
                ArtistBiography(
                    artist.getName(),
                    textToHtml(artist.getBiography(), artist.getName()),
                    artist.getArticleUrl()
                )
            }
        } catch (exception: Exception) {
            null
        }

    private fun String?.getArtist(): JsonObject {
        val jsonObject = Gson().fromJson(this, JsonObject::class.java)
        return jsonObject[ARTIST].asJsonObject
    }

    private fun JsonObject.getName() = this[NAME].asString

    private fun JsonObject.getBiography(): String {
        return this[BIOGRAPHY].asJsonObject[CONTENT].asString.replace("\\n", "\n")
    }

    private fun JsonObject.getArticleUrl() = this[URL].asString

    private fun saveToArticleDatabaseAsync(article: ArtistBiography) {
        Thread {
            saveToArticleDatabase(article)
        }.start()
    }

    private fun saveToArticleDatabase(article: ArtistBiography) {
        articleDatabase.ArticleDao().insertArticle(article.toArticleEntity())
    }

    private fun ArtistBiography.toArticleEntity() = ArticleEntity(this.artistName, this.biography, this.articleUrl)

    private fun updateViewAsync(article: ArtistBiography) {
        runOnUiThread {
            updateView(article)
        }
    }

    private fun updateView(article: ArtistBiography) {
        updateOpenUrlButton(article.articleUrl)
        updateLastFMLogoImageView()
        updateArticleTextView(article.biography)
    }

    private fun updateOpenUrlButton(articleUrl: String) {
        openUrlButton.setOnClickListener {
            onOpenUrlButtonClick(articleUrl)
        }
    }

    private fun onOpenUrlButtonClick(articleUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(articleUrl))
        startActivity(intent)
    }

    private fun updateLastFMLogoImageView() {
        Picasso.get().load(IMAGE_URL).into(lastFMLogoImageView)
    }

    private fun updateArticleTextView(biography: String) {
        articleTextView.text = Html.fromHtml(biography, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        const val IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
        const val NO_RESULTS = "No Results"
        const val ARTIST = "artist"
        const val NAME = "name"
        const val BIOGRAPHY = "bio"
        const val CONTENT = "content"
        const val URL = "url"

        fun textToHtml(text: String, term: String): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append("<html><div width=400>")
            stringBuilder.append("<font face=\"arial\">")
            stringBuilder.append(getTextWithBold(text, term))
            stringBuilder.append("</font></div></html>")
            return stringBuilder.toString()
        }

        private fun getTextWithBold(text: String, term: String): String {
            text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
                )
            return text
        }
    }
}
