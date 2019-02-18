package com.dz.ui.fragments.fileviewers

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.ActionBar
import androidx.core.os.bundleOf
import butterknife.BindView
import com.dz.libraries.utilities.MediaUtility
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import com.dz.utilities.PicasoUtility
import com.github.chrisbanes.photoview.PhotoView

class FileViewerFragment : BaseMainFragment<IFileViewerView, IFileViewerPresenter>(), IFileViewerView {
    companion object {
        const val PATH_FILE = "path_file"

        fun newBundle(path: String) = bundleOf(PATH_FILE to path)
    }

    @BindView(R.id.wvWebView)
    lateinit var wvWebView: WebView
    @BindView(R.id.ivImage)
    lateinit var ivImage: PhotoView
    @BindView(R.id.vvVideoView)
    lateinit var vvVideoView: VideoView
    @BindView(R.id.tvNoData)
    lateinit var tvNoData: ExtTextView

    var mediaControls: MediaController? = null

    override fun createPresenter(): IFileViewerPresenter = FileViewerPresenter(appComponent)

    override val resourceId: Int get() = R.layout.file_viewer_fragment

    override val titleId: Int get() = R.string.viewer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        initViews()
    }

    override fun initToolbar(supportActionBar: ActionBar) {
        super.initToolbar(supportActionBar)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setHomeAsUpIndicator(com.dz.ui.R.drawable.ic_back)
    }

    @SuppressLint("SetJavaScriptEnabled")
    internal fun initViews() {
        // no data
        tvNoData.visibility = View.GONE

        // get path
        val path = arguments?.getString(PATH_FILE) ?: ""
        if (StringUtility.isNullOrEmpty(path)) return
        Log.e("FileViewer", "Path:: $path")

        // hide view
        ivImage.visibility = View.GONE
        wvWebView.visibility = View.GONE
        vvVideoView.visibility = View.GONE

        when {
            MediaUtility.isImage(path) -> {
                ivImage.visibility = View.VISIBLE
                PicasoUtility.get()
                        .load(PicasoUtility.getLink(path))
                        .placeholder(R.drawable.bg_background_radius_component)
                        .into(ivImage)
            }
            MediaUtility.isVideo(path) -> {
                vvVideoView.visibility = View.VISIBLE

                //set the media controller buttons
                mediaControls = MediaController(mActivity)
                vvVideoView.setMediaController(mediaControls)
                vvVideoView.setVideoURI(Uri.parse(path))
                vvVideoView.requestFocus()

                showLoading(true)
                vvVideoView.setOnPreparedListener { mp ->
                    showLoading(false)
                    vvVideoView.start()
                }
            }
            else -> {
                // web view
                wvWebView.visibility = View.VISIBLE
                wvWebView.settings.allowFileAccess = true
                wvWebView.settings.javaScriptEnabled = true

                // loading
                wvWebView.webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                        super.onPageStarted(view, url, favicon)
                        showLoading(true)
                    }

                    @Suppress("OverridingDeprecatedMember", "DEPRECATION")
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        return super.shouldOverrideUrlLoading(view, url)
                    }

                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)
                        showLoading(false)
                    }
                }
                wvWebView.loadUrl(path)
            }
        }
    }
}