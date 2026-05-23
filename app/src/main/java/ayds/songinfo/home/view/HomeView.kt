package ayds.songinfo.home.view

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.R
import ayds.songinfo.home.model.HomeModel
import ayds.songinfo.home.model.HomeModelInjector
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song.SpotifySong
import ayds.songinfo.home.view.HomeUiState.Companion.DEFAULT_IMAGE
import ayds.songinfo.moredetails.presentation.MoreDetailsViewActivity
import ayds.songinfo.utils.UtilsInjector
import ayds.songinfo.utils.navigation.NavigationUtils
import ayds.songinfo.utils.view.ImageLoader

interface HomeView {
    val uiEventObservable: Observable<HomeUiEvent>
    val uiState: HomeUiState

    fun navigateToOtherDetails(artistName: String)
    fun openExternalLink(url: String)
}

class HomeViewActivity : AppCompatActivity(), HomeView {

    private val onActionSubject = Subject<HomeUiEvent>()
    private lateinit var homeModel: HomeModel
    private val songDescriptionHelper: SongDescriptionHelper = HomeViewInjector.songDescriptionHelper
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var searchButton: Button
    private lateinit var modeDetailsButton: Button
    private lateinit var openSongButton: Button
    private lateinit var termEditText: EditText
    private lateinit var descriptionTextView: TextView
    private lateinit var posterImageView: ImageView
    private lateinit var savedImageView: ImageView

    override val uiEventObservable: Observable<HomeUiEvent> = onActionSubject
    override var uiState: HomeUiState = HomeUiState()

    override fun navigateToOtherDetails(artistName: String) {
        val intent = Intent(this, MoreDetailsViewActivity::class.java)
        intent.putExtra(MoreDetailsViewActivity.ARTIST_NAME_EXTRA, artistName)
        startActivity(intent)
    }

    override fun openExternalLink(url: String) {
        if (uiState.songPreviewUrl.isNotEmpty()) {
            playSong(uiState.songPreviewUrl)
        } else {
            navigationUtils.openExternalUrl(this, url)
        }
    }

    private fun playSong(url: String) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener { start() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initModule()
        initProperties()
        initListeners()
        initObservers()
        updateSongImage()
    }

    private fun initModule() {
        HomeViewInjector.init(this)
        homeModel = HomeModelInjector.getHomeModel()
    }

    private fun initProperties() {
        searchButton = findViewById(R.id.searchButton)
        modeDetailsButton = findViewById(R.id.modeDetailsButton)
        openSongButton = findViewById(R.id.openSongButton)
        termEditText = findViewById(R.id.termEditText)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        posterImageView = findViewById(R.id.posterImageView)
        savedImageView = findViewById(R.id.savedImageView)
    }

    private fun initListeners() {
        searchButton.setOnClickListener {
            hideKeyboard(termEditText)
            searchAction()
        }
        modeDetailsButton.setOnClickListener { notifyMoreDetailsAction() }
        openSongButton.setOnClickListener { notifyOpenSongAction() }
    }

    private fun searchAction() {
        updateSearchTermState()
        updateDisabledActionsState()
        updateMoreDetailsState()
        notifySearchAction()
    }

    private fun notifySearchAction() {
        onActionSubject.notify(HomeUiEvent.Search)
    }

    private fun notifyMoreDetailsAction() {
        onActionSubject.notify(HomeUiEvent.MoreDetails)
    }

    private fun notifyOpenSongAction() {
        onActionSubject.notify(HomeUiEvent.OpenSongUrl)
    }

    private fun hideKeyboard(view: View) {
        (this@HomeViewActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun updateSearchTermState() {
        uiState = uiState.copy(searchTerm = termEditText.text.toString())
    }

    private fun updateDisabledActionsState() {
        uiState = uiState.copy(actionsEnabled = false)
    }

    private fun initObservers() {
        homeModel.songObservable
            .subscribe { value -> updateSongInfo(value) }
    }

    private fun updateSongInfo(song: Song) {
        updateUiState(song)
        updateSongDescription()
        updateSongImage()
        updateSavedIcon()
        updateMoreDetailsState()
        updateOpenSongButton()
    }

    private fun updateOpenSongButton() {
        runOnUiThread {
            openSongButton.text = if (uiState.songPreviewUrl.isNotEmpty()) "Play Preview" else "Open Song"
        }
    }

    private fun updateUiState(song: Song) {
        when (song) {
            is SpotifySong -> updateSongUiState(song)
            EmptySong -> updateNoResultsUiState()
        }
    }

    private fun updateSongUiState(song: SpotifySong) {
        uiState = uiState.copy(
            songId = song.id,
            songImageUrl = song.imageUrl,
            songUrl = song.spotifyUrl,
            songDescription = songDescriptionHelper.getSongDescriptionText(song),
            actionsEnabled = true,
            isLocallyStored = song.isLocallyStored,
            songPreviewUrl = song.previewUrl
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            songId = "",
            songImageUrl = DEFAULT_IMAGE,
            songUrl = "",
            songDescription = songDescriptionHelper.getSongDescriptionText(),
            actionsEnabled = false,
            isLocallyStored = false,
            songPreviewUrl = ""
        )
    }

    private fun updateSongDescription() {
        runOnUiThread {
            descriptionTextView.text = uiState.songDescription
        }
    }

    private fun updateSongImage() {
        runOnUiThread {
            imageLoader.loadImageIntoView(uiState.songImageUrl, posterImageView)
        }
    }

    private fun updateSavedIcon() {
        runOnUiThread {
            savedImageView.visibility = if (uiState.isLocallyStored) View.VISIBLE else View.GONE
        }
    }

    private fun updateMoreDetailsState() {
        enableActions(uiState.actionsEnabled)
    }

    private fun enableActions(enable: Boolean) {
        runOnUiThread {
            modeDetailsButton.isEnabled = enable
            openSongButton.isEnabled = enable
        }
    }
}