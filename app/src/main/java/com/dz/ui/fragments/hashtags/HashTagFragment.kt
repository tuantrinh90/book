package com.dz.ui.fragments.hashtags

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.dz.customizes.views.edittexts.EditTextApp
import com.dz.libraries.models.IModel
import com.dz.libraries.utilities.CollectionUtility
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.views.recyclerviews.ExtRecyclerView
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.models.events.HashTagEvent
import com.dz.models.responses.HashTagResponse
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import com.dz.ui.fragments.hashtags.adapters.HashTagAdapter
import com.dz.ui.fragments.hashtags.adapters.ResultAdapter
import com.dz.ui.fragments.hashtags.dialogs.HashTagActionDialogFragment

class HashTagFragment : BaseMainFragment<IHashTagView, IHashTagPresenter>(), IHashTagView {
    @BindView(R.id.etSearch)
    lateinit var etSearch: EditTextApp
    @BindView(R.id.rvHashtag)
    lateinit var rvHashtag: RecyclerView
    @BindView(R.id.vLineHashTag)
    lateinit var vLineHashTag: View
    @BindView(R.id.rvResult)
    lateinit var rvResult: ExtRecyclerView<HashTagResponse, ResultAdapter.ViewHolder>

    var hashTagAdapter: HashTagAdapter? = null
    var hashtags = ArrayList<HashTagResponse?>()

    override fun createPresenter(): IHashTagPresenter = HashTagPresenter(appComponent)

    override val resourceId: Int get() = R.layout.hash_tag_fragment

    override val titleId: Int get() = R.string.hash_tag

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        initViews()
    }

    override fun initToolbar(supportActionBar: ActionBar) {
        super.initToolbar(supportActionBar)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun setData(response: ArrayList<HashTagResponse?>?) {
        rvResult.addItems(response)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getExtPagingListView(): ExtRecyclerView<IModel, ExtRecyclerViewHolder>? = rvResult as ExtRecyclerView<IModel, ExtRecyclerViewHolder>

    fun initViews() {
        // text change
        etSearch.textChangeConsumer = { text -> etSearch.iconRightImageView.visibility = if (StringUtility.isNullOrEmpty(text)) View.INVISIBLE else View.VISIBLE }
        etSearch.iconRightImageView.visibility = View.INVISIBLE
        etSearch.iconRightImageView.setOnClickListener { etSearch.setContent("") }
        // edit text search listener
        etSearch.etContent.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onRefresh()
                return@OnEditorActionListener true
            }
            false
        })

        // hashtags
        hashTagAdapter = HashTagAdapter(mActivity, hashtags) {
            hashTagAdapter?.removeItem(it)
            displayHashTag()
        }
        rvHashtag.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rvHashtag.adapter = hashTagAdapter

        // results
        rvResult
                .setAdapter(ResultAdapter(mActivity, null) {
                    hashTagAdapter?.addItem(it)
                    displayHashTag()
                })
                .setRefreshListener { onRefresh() }
                .setLoadMoreListener { loadData() }
                .build()

        // display data
        onRefresh()
    }

    fun displayHashTag() {
        vLineHashTag.visibility = if (hashTagAdapter!!.itemCount > 0) View.VISIBLE else View.GONE
        rvHashtag.visibility = if (hashTagAdapter!!.itemCount > 0) View.VISIBLE else View.GONE
        distinctHashTag()
    }

    fun onRefresh() {
        rvResult.clear()
        displayHashTag()
        loadData()
    }

    fun loadData() {
        presenter.getHashTags(etSearch.getContent(), rvResult.itemCount)
    }

    fun distinctHashTag(){
        // distinct item
        val items = hashTagAdapter?.getItems()
        CollectionUtility.with(items).doIfPresent { its ->
            val results = its.distinctBy { d -> d?.name } as? ArrayList<HashTagResponse?>?
            hashTagAdapter?.setItems(results)
        }
    }

    @OnClick(R.id.ivAdd)
    fun onClickAdd() {
        HashTagActionDialogFragment()
                .setConsumer {
                    hashTagAdapter?.addItems(it)
                    displayHashTag()
                    onRefresh()
                }
                .show(fragmentManager, null)
    }

    @OnClick(R.id.tvConfirm)
    fun onClickConfirm() {
        if (hashTagAdapter!!.itemCount <= 0) {
            showToastError(getString(R.string.error_valid_hash_tag))
            return
        }

        rxBus.send(HashTagEvent(hashTagAdapter?.getItems()))
        mActivity.finish()
    }
}