package com.dz.ui.fragments.contests.lists

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import butterknife.BindView
import butterknife.OnClick
import com.dz.commons.activities.alonefragment.AloneFragmentActivity
import com.dz.libraries.models.IModel
import com.dz.libraries.utilities.MediaUtility
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.views.keypairs.ExtKeyPair
import com.dz.libraries.views.recyclerviews.ExtRecyclerView
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.models.ArrayResponse
import com.dz.models.responses.ContestResponse
import com.dz.models.responses.UploadResponse
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import com.dz.ui.fragments.contests.lists.adapters.ListContestAdapter
import com.dz.ui.fragments.upload.UploadVideoFragment
import com.dz.utilities.AppUtility
import com.dz.utilities.Constant
import io.reactivex.disposables.CompositeDisposable
import java.io.File

class ListContestFragment : BaseMainFragment<IListContestView, IListContestPresenter>(), IListContestView {
    enum class Type {
        OPEN, CLOSE
    }

    @BindView(R.id.tvNewest)
    lateinit var tvNewest: ExtTextView
    @BindView(R.id.tvClosed)
    lateinit var tvClosed: ExtTextView
    @BindView(R.id.tvTotalContent)
    lateinit var tvTotalContent: ExtTextView
    @BindView(R.id.rvResult)
    lateinit var rvResult: ExtRecyclerView<ContestResponse, ListContestAdapter.ViewHolder>

    var sortByTime: ExtKeyPair? = null
    var sortByStatus: ExtKeyPair? = null
    var mType = Type.OPEN
    var filePath: File? = null
    var contestId: Int? = 0

    override fun createPresenter(): IListContestPresenter = ListContestPresenter(appComponent)

    override val resourceId: Int get() = R.layout.list_contest_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        initViews()
    }

    @Suppress("UNCHECKED_CAST")
    override fun getExtPagingListView(): ExtRecyclerView<IModel, ExtRecyclerViewHolder>? {
        return rvResult as ExtRecyclerView<IModel, ExtRecyclerViewHolder>
    }

    override fun getCompositeDisposable(): CompositeDisposable {
        return mCompositeDisposable
    }

    override fun setData(response: ArrayResponse<ContestResponse>?) {
        response?.let {
            rvResult.addItems(it.items)
            displayTotalRecords(it.total ?: 0)
        }
    }

    fun getMediaDuration(uriOfFile: Uri): Int {
        val mp = MediaPlayer.create(appContext, uriOfFile)
        return mp.getDuration()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AppUtility.actionVideoCameraOrGalleryOnActivityResult(mActivity, requestCode, resultCode, data, filePath) {
            presenter.uploadFile(it)
        }
    }

    override fun onUploadSuccess(response: UploadResponse?) {
        if (response == null || StringUtility.isNullOrEmpty(response.file)) return
        //FragmentUtility.replaceFragment(mActivity, TrimerFragment.newInstance(it.toString(), getMediaDuration(Uri.fromFile(it))))
        AloneFragmentActivity.with(this)
                .parameters(UploadVideoFragment.newBundle(response, contestId!!))
                .start(UploadVideoFragment::class.java)
    }

    fun initViews() {
        // default value
        sortByTime = ExtKeyPair(Constant.SORT_NEWEST, getString(R.string.newest_to_oldest))
        sortByStatus = ExtKeyPair(Constant.FILTER_NOT_JOINED_YET, getString(R.string.not_joined_yet))

        // display data
        displayConditionFilter()

        // adapter
        rvResult
                .setAdapter(ListContestAdapter(mActivity, null, {
                    contestId = it!!.id
                    filePath = MediaUtility.getVideoUrlMp4()
                    AppUtility.actionVideoCameraOrGallery(mCompositeDisposable, mActivity, this@ListContestFragment, filePath!!) {
                        // TODO: action choose video from library app
                    }
                }, {
                    // TODO: item click
                }))
                .setRefreshListener { onRefresh() }
                .setLoadMoreListener { loadData() }
                .build()

        // load data
        onRefresh()
    }

    fun displayTotalRecords(total: Int) {
        tvTotalContent.text = getString(R.string.total_contest).format(total)
        tvTotalContent.visibility = if (total > 0) View.VISIBLE else View.GONE
    }

    fun displayConditionFilter() {
        tvNewest.text = sortByTime?.value ?: ""
        tvClosed.text = sortByStatus?.value ?: ""
    }

    fun onRefresh() {
        rvResult.clear()
        displayTotalRecords(0)
        loadData()
    }

    fun loadData() {
        presenter.getData(if (mType == Type.OPEN) Constant.OPEN else Constant.CLOSED, sortByTime?.key
                ?: "", sortByStatus?.key ?: "", rvResult.itemCount)
    }

    @OnClick(R.id.tvNewest)
    fun onClickNewest() {
        AppUtility.showPopUpWindow(mActivity, tvNewest, sortByTime, AppUtility.getSortByTime(mActivity)) {
            sortByTime = it
            displayConditionFilter()
            onRefresh()
        }
    }

    @OnClick(R.id.tvClosed)
    fun onClickClosed() {
        AppUtility.showPopUpWindow(mActivity, tvClosed, sortByStatus, AppUtility.getSortByStatus(mActivity)) {
            sortByStatus = it
            displayConditionFilter()
            onRefresh()
        }
    }

    fun setType(type: Type): ListContestFragment {
        mType = type
        return this
    }

    fun setCompositeDisposable(compositeDisposable: CompositeDisposable): ListContestFragment {
        mCompositeDisposable = compositeDisposable
        return this
    }
}