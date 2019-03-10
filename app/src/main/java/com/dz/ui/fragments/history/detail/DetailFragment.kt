package com.dz.ui.fragments.history

import android.Manifest
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.dz.models.responses.BookResponse
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import com.dz.ui.fragments.history.detail.adapter.DetailAdapter
import com.dz.utilities.Constant
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.models.BookDetail
import com.dz.models.database.Book
import com.dz.ui.BuildConfig
import com.dz.utilities.AppUtility
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import java.io.File


class DetailFragment : BaseMainFragment<IDetailView, IDetailPresenter>(), IDetailView {

    companion object {
        fun newBundle(id: Int): Bundle = bundleOf(Constant.KEY_INTENT_DETAIL to id)
    }

    @BindView(R.id.rvLinks)
    lateinit var rvLinks: RecyclerView

    @BindView(R.id.tvType)
    lateinit var tvType: ExtTextView

    @BindView(R.id.tvPublishing)
    lateinit var tvPublishing: ExtTextView

    @BindView(R.id.tvAuthor)
    lateinit var tvAuthor: ExtTextView

    @BindView(R.id.tvTitle)
    lateinit var tvTitle: ExtTextView

    lateinit var detailAdapter: DetailAdapter

    lateinit var transaction: FragmentTransaction

    override fun createPresenter(): IDetailPresenter = DetailPresenter(appComponent, mActivity)

    override val resourceId: Int get() = R.layout.history_detail_fragment

    override val titleId: Int get() = R.string.detail

    private var VIDEO_ID = ""
    private var book: Book? = null

    var isFavorite: Boolean = false
    lateinit var downloadManager: DownloadManager
    var list = ArrayList<Long>()
    var refid: Long? = 0
    lateinit var menuDownload: MenuItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        setHasOptionsMenu(true)
        val id: Int = arguments?.get(Constant.KEY_INTENT_DETAIL) as Int
        presenter.getBookById(id)
    }

    fun initYoutubePlayer(videoId: String) {
        val youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance()
        transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.youtube_layout, youTubePlayerFragment as Fragment).commit()
        youTubePlayerFragment.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider, player: YouTubePlayer, p2: Boolean) {
                player.setFullscreen(false)
                player.loadVideo(videoId)
                player.play()
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, error: YouTubeInitializationResult?) {
                val errorMessage = error.toString()
                showToastError(errorMessage)
                Log.d("errorMessage:", errorMessage)
            }

        })
    }

    fun initView() {
        isStoragePermissionGranted()
        downloadManager = context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        context!!.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        detailAdapter = DetailAdapter(mActivity, null) {
            val link = it!!.link!!
            if (!StringUtility.isNullOrEmpty(link)) {
                if (link.contains("v="))
                    initYoutubePlayer(link.split("v=")[1])
            }
        }
        rvLinks.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvLinks.isNestedScrollingEnabled = false
        rvLinks.adapter = detailAdapter

        tvAuthor.text = book!!.author
        tvTitle.text = book!!.description
        tvPublishing.text = book!!.publishing
        tvType.text = book!!.type

    }

    override fun setData(response: ArrayList<BookResponse?>?) {
        //detailAdapter?.setItems(response)
    }

    override fun getBookDetail(bookDetail: List<BookDetail?>?) {
        detailAdapter?.setItems(bookDetail as ArrayList<BookDetail?>)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_detail, menu)
        val icon = menu!!.findItem(R.id.action_favotite)
        icon.setIcon(if (isFavorite) R.drawable.ic_star_selected else R.drawable.ic_star_unselected)
        menuDownload = menu!!.findItem(R.id.action_download)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_favotite -> {
                selectedFavorite(item)
            }
            R.id.action_download -> {
                acctionDownload()
            }
            R.id.action_share -> {
                AppUtility.shareSocial(mActivity)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initToolbar(supportActionBar: ActionBar) {
        super.initToolbar(supportActionBar)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    // check selected isFavorite
    fun selectedFavorite(item: MenuItem?) {
        isFavorite = !book!!.favorite
        item!!.setIcon(if (isFavorite) R.drawable.ic_star_selected else R.drawable.ic_star_unselected)
        book?.favorite = isFavorite
        presenter.updateFavorite(book as Book)
    }

    // get book by ID
    override fun getBook(book: Book) {
        this.book = book
        isFavorite = book.favorite
        initView()
        initYoutubePlayer(VIDEO_ID)
        presenter.setBookDetail()
        if (list.isEmpty()) menuDownload.isVisible = false
    }

    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context!!.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    === PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                ActivityCompat.requestPermissions(mActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true
        }
    }

    internal var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            list.remove(referenceId)
            Log.e("IN", "" + referenceId)
            if (list.isEmpty()) {
                val mBuilder = NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Download Book")
                        .setContentText("All Download completed")
                val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(455, mBuilder.build())
                menuDownload.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        context!!.unregisterReceiver(onComplete)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }

    fun acctionDownload() {
        list.clear()
        if (detailAdapter == null) return
        for (bookDetail in detailAdapter.getItems()!!) {
            var downloadUri = Uri.parse(bookDetail!!.link)
            var folderPath = File(Environment.getExternalStorageDirectory().toString() + "/" + "DownloadBook")
            if (!folderPath.exists())
                folderPath.mkdirs()
            val filePath = folderPath.toString() + "/" + downloadUri.lastPathSegment
            FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID + ".provider", File(filePath))
            val destinationUri = Uri.fromFile(File(filePath))
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(false)
            request.setTitle(bookDetail!!.link)
            request.setDescription(bookDetail!!.link)
            request.setVisibleInDownloadsUi(true)
            request.setDestinationUri(destinationUri)
            refid = downloadManager.enqueue(request)
            Log.e("OUTNM", "" + refid)
            list.add(refid!!)
        }
    }

}