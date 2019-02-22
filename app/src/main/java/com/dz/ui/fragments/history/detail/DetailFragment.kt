package com.dz.ui.fragments.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
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
import androidx.fragment.app.FragmentTransaction
import com.dz.libraries.utilities.StringUtility
import com.google.android.youtube.player.YouTubePlayerSupportFragment


class DetailFragment : BaseMainFragment<IDetailView, IDetailPresenter>(), IDetailView {
    @BindView(R.id.rvLinks)
    lateinit var rvLinks: RecyclerView
    var detailAdapter: DetailAdapter? = null
    lateinit var transaction: FragmentTransaction

    override fun createPresenter(): IDetailPresenter = DetailPresenter(appComponent)

    override val resourceId: Int get() = R.layout.history_detail_fragment

    override val titleId: Int get() = R.string.detail
    private var VIDEO_ID = ""

    var favorite: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        setHasOptionsMenu(true)
        initView()
        initYoutubePlayer()
    }

    fun initYoutubePlayer() {
        val youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance()
        transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit()
        youTubePlayerFragment.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider, player: YouTubePlayer, p2: Boolean) {
                //TODO video id youtube
                VIDEO_ID = "ZLNO2c7nqjw"
                player.setFullscreen(false)
                player.loadVideo(VIDEO_ID)
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
        detailAdapter = DetailAdapter(mActivity, null) {
            val link = it!!.link!!
            if (!StringUtility.isNullOrEmpty(link)) {
                if (link.contains("v="))
                    VIDEO_ID = link.split("v=")[1]
                initYoutubePlayer()
            }
        }
        rvLinks.layoutManager = GridLayoutManager(mActivity, 2)
        rvLinks.isNestedScrollingEnabled = false
        rvLinks.adapter = detailAdapter
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun loadData() {
        presenter.getData()
    }

    // check selected favorite
    fun selectedFavorite(item: MenuItem?) {
        item!!.setIcon(if (favorite) R.drawable.ic_star_selected else R.drawable.ic_star_unselected)
        favorite = !favorite
    }

    override fun setData(response: ArrayList<BookResponse?>?) {
        detailAdapter?.setItems(response)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_favotite -> {
                selectedFavorite(item)
            }
            R.id.action_download -> {

            }
            R.id.action_share -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initToolbar(supportActionBar: ActionBar) {
        super.initToolbar(supportActionBar)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back)
    }

}