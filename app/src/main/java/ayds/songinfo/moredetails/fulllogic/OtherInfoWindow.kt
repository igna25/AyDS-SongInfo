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
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

class OtherInfoWindow : Activity() {
    private var textPane1: TextView? = null
    private var articleDatabase: ArticleDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initTextPane()
        openArticleDatabase(intent.getStringExtra("artistName"))
    }

    private fun initTextPane() {
        textPane1 = findViewById(R.id.textPane1)
    }

    private fun openArticleDatabase(artist: String?) {
        articleDatabase =
            databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        Thread {
            articleDatabase!!.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
            Log.e("TAG", "" + articleDatabase!!.ArticleDao().getArticleByArtistName("test"))
            Log.e("TAG", "" + articleDatabase!!.ArticleDao().getArticleByArtistName("nada"))
        }.start()
        getArtistInfo(artist!!)
    }

    fun getArtistInfo(artistName: String) {
        Log.e("TAG", "artistName $artistName")
        Thread {
            val text = s(artistName)
            val imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
            Log.e("TAG", "Get Image from $imageUrl")
            runOnUiThread {
                Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView1) as ImageView)
                textPane1!!.text = Html.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }.start()
    }

    private fun s(artistName: String): String {
        var article = articleDatabase!!.ArticleDao().getArticleByArtistName(artistName)
        var text = "[*]"
        if (article == null) {
            article = getFromService(artistName)
            text = ""
        }
        text += article.biography
        setButtonUrl(article)
        return text
    }

    private fun getFromService(artistName: String?): ArticleEntity{
        val article = getArticleEntityFromService(artistName!!)
        if (article.biography != "No Results") {
            saveToDatabase(article)
        }
        return article
    }

    private fun getArticleEntityFromService(artistName: String): ArticleEntity{
        var content : JsonElement? = null
        var url : JsonElement? = null
        try {
            val lastFMAPI = createLastFMAPI()
            val callResponse = lastFMAPI.getArtistInfo(artistName).execute()
            Log.e("TAG", "JSON " + callResponse.body())
            val gson = Gson()
            val jsonObject = gson.fromJson(callResponse.body(), JsonObject::class.java)
            val artist = jsonObject["artist"].getAsJsonObject()
            val bio = artist["bio"].getAsJsonObject()
            content = bio["content"]
            url = artist["url"]
        } catch (ioException: IOException) {
            Log.e("TAG", "Error $ioException")
            ioException.printStackTrace()
        }

        return ArticleEntity(artistName, textToHtml(content?.asString?.replace("\\n", "\n") ?: "No results",artistName), url?.asString ?: "")
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
            articleDatabase!!.ArticleDao().insertArticle(article)
        }
            .start()
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
