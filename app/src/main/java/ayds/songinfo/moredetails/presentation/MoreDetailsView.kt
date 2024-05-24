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
import ayds.songinfo.utils.UtilsInjector
import ayds.songinfo.utils.navigation.NavigationUtils
import ayds.songinfo.utils.view.ImageLoader

class MoreDetailsViewActivity : Activity() {
    private lateinit var descriptionTextView: TextView
    private lateinit var lastFMLogoImageView: ImageView
    private lateinit var openUrlButton: Button
    private lateinit var sourceTextView: TextView

    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils

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
        moreDetailsPresenter = MoreDetailsInjector.moreDetailsPresenter
    }

    private fun initViewProperties() {
        descriptionTextView = findViewById(R.id.description1TextView)
        lastFMLogoImageView = findViewById(R.id.logo1ImageView)
        openUrlButton = findViewById(R.id.openUrl1Button)
        sourceTextView = findViewById(R.id.source1TextView)
    }

    private fun initObservers() {
        moreDetailsPresenter.artistBiographyObservable
            .subscribe{ value -> updateViewAsync(value)}
    }

    private fun updateViewAsync(article: CardUiState) {
        runOnUiThread {
            updateView(article)
        }
    }

    private fun updateView(article: CardUiState) {
        updateOpenUrlButton(article.infoUrl)
        updateLastFMLogoImageView(article.sourceLogoUrl)
        updateDescriptionTextView(article.infoHtml)
        updateSourceTextView(article.source)
    }

    private fun updateOpenUrlButton(infoUrl: String) {
        openUrlButton.setOnClickListener {
            onOpenUrlButtonClick(infoUrl)
        }
    }

    private fun onOpenUrlButtonClick(infoUrl: String) {
        navigationUtils.openExternalUrl(this, infoUrl)
    }

    private fun updateLastFMLogoImageView(sourceLogoUrl: String) {
        imageLoader.loadImageIntoView(sourceLogoUrl, lastFMLogoImageView)
    }

    private fun updateDescriptionTextView(infoHtml: String) {
        descriptionTextView.text = Html.fromHtml(infoHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun updateSourceTextView(source: String) {
        sourceTextView.text = source
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
    }
}
