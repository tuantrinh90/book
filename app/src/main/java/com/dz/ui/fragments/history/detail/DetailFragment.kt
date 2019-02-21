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
import com.google.android.youtube.player.YouTubePlayerView

class DetailFragment : BaseMainFragment<IDetailView, IDetailPresenter>(), IDetailView {
//    @BindView(R.id.youtubePlay)
//    lateinit var youtubePlay: YouTubePlayerView
    @BindView(R.id.rvLinks)
    lateinit var rvLinks: RecyclerView
    var detailAdapter: DetailAdapter? = null
    var onInitializedListener: YouTubePlayer.OnInitializedListener? = null

    override fun createPresenter(): IDetailPresenter = DetailPresenter(appComponent)

    override val resourceId: Int get() = R.layout.history_detail_fragment

    override val titleId: Int get() = R.string.detail


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        setHasOptionsMenu(true)
        initView()
        //initYoutubePlayer()
    }

    fun initYoutubePlayer() {
        onInitializedListener = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {
                youTubePlayer.loadVideo("https://www.youtube.com/watch?v=W4hTJybfU7s")
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {
            }
        }
//        youtubePlay.initialize(Constant.API_KEY, onInitializedListener)

    }

    fun initView() {
        detailAdapter = DetailAdapter(mActivity, null) {}
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