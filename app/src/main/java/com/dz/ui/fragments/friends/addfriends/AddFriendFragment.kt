package com.dz.ui.fragments.friends.addfriends

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import butterknife.BindView
import butterknife.OnClick
import com.dz.customizes.views.edittexts.EditTextApp
import com.dz.libraries.models.IModel
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.views.recyclerviews.ExtRecyclerView
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.models.events.AddFriendEvent
import com.dz.models.responses.MemberResponse
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import com.dz.ui.fragments.friends.addfriends.adapters.AddFriendAdapter

class AddFriendFragment : BaseMainFragment<IAddFriendView, IAddFriendPresenter>(), IAddFriendView {
    @BindView(R.id.etSearch)
    lateinit var etSearch: EditTextApp
    @BindView(R.id.rvResult)
    lateinit var rvResult: ExtRecyclerView<MemberResponse, AddFriendAdapter.ViewHolder>

    override fun createPresenter(): IAddFriendPresenter = AddFriendPresenter(appComponent)

    override val resourceId: Int get() = R.layout.add_friend_fragment

    override val titleId: Int get() = R.string.add_friends

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

    @Suppress("UNCHECKED_CAST")
    override fun getExtPagingListView(): ExtRecyclerView<IModel, ExtRecyclerViewHolder>? = rvResult as ExtRecyclerView<IModel, ExtRecyclerViewHolder>

    override fun setData(response: ArrayList<MemberResponse?>?) {
        rvResult.addItems(response)
    }

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

        // results
        rvResult.setAdapter(AddFriendAdapter(mActivity, null, false, {}, {}))
                .setRefreshListener { onRefresh() }
                .setLoadMoreListener { loadData() }
                .build()

        // display data
        onRefresh()
    }


    fun onRefresh() {
        rvResult.clear()
        loadData()
    }

    fun loadData() {
        presenter.getFriends(etSearch.getContent(), rvResult.itemCount)
    }

    fun isValid(): Boolean {
        if (rvResult.itemCount <= 0 || rvResult.items!!.count { it!!.isChecked } <= 0) {
            showToastError(getString(R.string.error_valid_add_friend))
            return false
        }

        return true
    }

    @OnClick(R.id.tvConfirm)
    fun onClickConfirm() {
        if (!isValid()) return

        val members = rvResult.items!!.filter { it!!.isChecked } as? ArrayList<MemberResponse?>
        rxBus.send(AddFriendEvent(members))
        mActivity.finish()
    }
}