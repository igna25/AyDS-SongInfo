package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.os.Bundle
import android.text.Html
import android.view.View
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
    private lateinit var descriptionTextViews: List<TextView>
    private lateinit var sourceLogoImageViews: List<ImageView>
    private lateinit var openUrlButtons: List<Button>
    private lateinit var sourceTextViews: List<TextView>

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
        descriptionTextViews = listOf(
            findViewById(R.id.description1TextView),
            findViewById(R.id.description2TextView),
            findViewById(R.id.description3TextView),
        )

        sourceLogoImageViews = listOf(
            findViewById(R.id.logo1ImageView),
            findViewById(R.id.logo2ImageView),
            findViewById(R.id.logo3ImageView),
        )

        openUrlButtons = listOf(
            findViewById(R.id.openUrl1Button),
            findViewById(R.id.openUrl2Button),
            findViewById(R.id.openUrl3Button),
        )

        sourceTextViews = listOf(
            findViewById(R.id.source1TextView),
            findViewById(R.id.source2TextView),
            findViewById(R.id.source3TextView),
        )
    }

    private fun initObservers() {
        moreDetailsPresenter.cardsObservable
            .subscribe{ value -> updateViewAsync(value)}
    }

    private fun updateViewAsync(cards: List<CardUiState>) {
        runOnUiThread {
            updateView(cards)
        }
    }

    private fun updateView(cards: List<CardUiState>) {
        updateOpenUrlButtons(cards.map { it.infoUrl })
        updateSourceLogoImageViews(cards.map { it.sourceLogoUrl })
        updateDescriptionTextViews(cards.map { it.infoHtml })
        updateSourceTextViews(cards.map { it.source })

    }

    private fun updateOpenUrlButtons(infoUrls: List<String>) {
        infoUrls.forEachIndexed { index, infoUrl ->
            updateOnClick(openUrlButtons[index], infoUrl)
            updateVisibility(openUrlButtons[index])
        }
    }

    private fun updateOnClick(button: Button, infoUrl: String){
        button.setOnClickListener {
            onOpenUrlButtonClick(infoUrl)
        }
    }

    private fun onOpenUrlButtonClick(infoUrl: String) {
        navigationUtils.openExternalUrl(this, infoUrl)
    }

    private fun updateVisibility(button: Button){
        button.visibility = View.VISIBLE
    }

    private fun updateSourceLogoImageViews(sourceLogoUrls: List<String>) {
        sourceLogoUrls.forEachIndexed{ index, sourceLogoUrl ->
            imageLoader.loadImageIntoView(sourceLogoUrl, sourceLogoImageViews[index])
        }
    }

    private fun updateDescriptionTextViews(infoHtmls: List<String>) {
        infoHtmls.forEachIndexed { index, infoHtml ->
            descriptionTextViews[index].text =
                Html.fromHtml(infoHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun updateSourceTextViews(sources: List<String>) {
        sources.forEachIndexed { index, source ->
            sourceTextViews[index].text = source
        }
    }

    private fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        moreDetailsPresenter.searchArtistDetails(getArtistName())
    }

    private fun getArtistName() = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: ""

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
