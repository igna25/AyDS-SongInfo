package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import ayds.songinfo.R
import ayds.songinfo.moredetails.injector.MoreDetailsInjector
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.utils.UtilsInjector
import ayds.songinfo.utils.navigation.NavigationUtils
import ayds.songinfo.utils.view.ImageLoader

class MoreDetailsViewActivity : Activity() {
    private lateinit var articleTextView: TextView
    private lateinit var lastFMLogoImageView: ImageView
    private lateinit var openUrlButton: Button

    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils

    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper

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
        moreDetailsPresenter = MoreDetailsInjector.moreDetailsPresenter
        artistBiographyDescriptionHelper = MoreDetailsInjector.artistBiographyDescriptionHelper
    }

    private fun initViewProperties() {
        articleTextView = findViewById(R.id.textPane1)
        lastFMLogoImageView = findViewById(R.id.imageView1)
        openUrlButton = findViewById(R.id.openUrlButton1)
    }

    private fun initObservers() {
        moreDetailsPresenter.artistBiographyObservable
            .subscribe{ value -> updateViewAsync(adaptArticle(value))}
    }

    private fun adaptArticle(article: ArtistBiography) = article.copy(biography = artistBiographyDescriptionHelper.getDescription(article))

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
        navigationUtils.openExternalUrl(this, articleUrl)
    }

    private fun updateLastFMLogoImageView() {
        imageLoader.loadImageIntoView(IMAGE_URL, lastFMLogoImageView)
    }

    private fun updateArticleTextView(biography: String) {
        articleTextView.text = Html.fromHtml(biography, HtmlCompat.FROM_HTML_MODE_LEGACY)
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

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        const val IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }
}
