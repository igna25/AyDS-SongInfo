package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import ayds.songinfo.R
import ayds.songinfo.moredetails.MoreDetailsInjector
import ayds.songinfo.moredetails.domain.ArtistBiography
import com.squareup.picasso.Picasso
import java.util.Locale

interface MoreDetailsView{
}

class MoreDetailsViewActivity : Activity(), MoreDetailsView {
    private lateinit var articleTextView: TextView
    private lateinit var lastFMLogoImageView: ImageView
    private lateinit var openUrlButton: Button

    private lateinit var moreDetailsPresenter: MoreDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initViewProperties()
        initObservers()

        getArtistInfoAsync()
    }

    private fun initModule(){
        MoreDetailsInjector.init(this)
        moreDetailsPresenter = MoreDetailsInjector.getMoreDetailsPresenter()
    }

    private fun initViewProperties() {
        articleTextView = findViewById(R.id.textPane1)
        lastFMLogoImageView = findViewById(R.id.imageView1)
        openUrlButton = findViewById(R.id.openUrlButton1)
    }

    private fun initObservers() {
        moreDetailsPresenter.artistBiographyObservable
            .subscribe{ value -> updateViewAsync(value)}
    }

    private fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        moreDetailsPresenter.searchArtistBiography(getArtistName())
    }

    private fun getArtistName() = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: ""

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
        moreDetailsPresenter.openExternalLink(this, articleUrl)
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
