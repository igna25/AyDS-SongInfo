package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
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

class OtherInfoWindow : Activity() {
    private lateinit var textPane1: TextView
    private lateinit var articleDatabase: ArticleDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initTextPane()
        openArticleDatabase(intent.getStringExtra(ARTIST_NAME_EXTRA) ?: "")
    }

    private fun initTextPane() {
        textPane1 = findViewById(R.id.textPane1)
    }

    private fun openArticleDatabase(artist: String) {
        articleDatabase =
            databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        testDatabase()
        getArtistInfo(artist)
    }

    private fun testDatabase() {
        Thread {
            articleDatabase.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
            Log.e("TAG", "" + articleDatabase.ArticleDao().getArticleByArtistName("test"))
            Log.e("TAG", "" + articleDatabase.ArticleDao().getArticleByArtistName("nada"))
        }.start()
    }

    private fun getArtistInfo(artistName: String) {
        Log.e("TAG", "artistName $artistName")
        Thread {
            updateView(getArticleText(artistName))
        }.start()
    }

    private fun updateView(text: String) {
        Log.e("TAG", "Get Image from $IMAGE_URL")
        runOnUiThread {
            Picasso.get().load(IMAGE_URL).into(findViewById<View>(R.id.imageView1) as ImageView)
            textPane1.text = Html.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun getArticleText(artistName: String): String {
        var article = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        var text = "[*]"
        if (article == null) {
            article = getFromService(artistName)
            text = ""
        }
        text += article.biography
        setButtonUrl(article)
        return text
    }

    private fun getFromService(artistName: String): ArticleEntity{
        val article = getArticleFromService(artistName)
        if (article.biography != "No Results") {
            saveToDatabase(article)
        }
        return article
    }

    private fun getArticleFromService(artistName: String): ArticleEntity{
        val callResponse = getJsonFromService(artistName)
        return getArticleFromJson(callResponse, artistName)
    }

    private fun getArticleFromJson(callResponse: Response<String>, artistName: String): ArticleEntity {
        val jsonObject = Gson().fromJson(callResponse.body(), JsonObject::class.java)
        val artist = jsonObject["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val content = bio["content"]
        val url = artist["url"]
        val contentString = content.asString.replace("\\n", "\n").ifBlank { "No Results" }

        return ArticleEntity(
            artistName,
            textToHtml(contentString, artistName),
            url.asString
        )
    }

    private fun getJsonFromService(artistName: String): Response<String> {
        val callResponse = createLastFMAPI().getArtistInfo(artistName).execute()
        Log.e("TAG", "JSON " + callResponse.body())
        return callResponse
    }

    private fun createLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(LastFMAPI::class.java)
    }

    private fun saveToDatabase(article : ArticleEntity) {
        Thread {
            articleDatabase.ArticleDao().insertArticle(article)
        }.start()
    }

    private fun setButtonUrl(article: ArticleEntity) {
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(article.articleUrl))
            startActivity(intent)
        }
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        const val IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
        fun textToHtml(text: String, term: String?): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append("<html><div width=400>")
            stringBuilder.append("<font face=\"arial\">")
            stringBuilder.append(getTextWithBold(text, term))
            stringBuilder.append("</font></div></html>")
            return stringBuilder.toString()
        }

        private fun getTextWithBold(text: String, term: String?): String {
            text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
                )
            return text
        }
    }
}
