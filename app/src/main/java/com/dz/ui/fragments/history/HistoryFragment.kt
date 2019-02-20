package com.dz.ui.fragments.history

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.BindView
import com.dz.commons.activities.alonefragment.AloneFragmentActivity
import com.dz.libraries.views.recyclerviews.ExtRecyclerView
import com.dz.models.responses.BookResponse
import com.dz.models.responses.HashTagResponse
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import com.dz.ui.fragments.history.adapter.HistoryAdapter
import com.dz.utilities.FragmentUtility
import io.reactivex.disposables.CompositeDisposable

class HistoryFragment : BaseMainFragment<IHistoryView, IHistoryPresenter>(), IHistoryView {


    @BindView(R.id.list_book)
    lateinit var rvResult: ExtRecyclerView<BookResponse, HistoryAdapter.ViewHolder>

    var historyAdapter: HistoryAdapter? = null

    override fun createPresenter(): IHistoryPresenter = HistoryPresenter(appComponent)

    override val resourceId: Int get() = R.layout.history_fragment

    override val titleId: Int get() = R.string.home

    override fun setData(response: ArrayList<BookResponse?>?) {
        rvResult.addItems(response)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        initView()
        loadData()
    }

    fun initView() {
        rvResult.setLayoutManager(GridLayoutManager(context, 2))
        rvResult
                .setAdapter(HistoryAdapter(mActivity, null) {
                    historyAdapter?.addItem(it)
                    AloneFragmentActivity.with(mActivity).start(DetailFragment::class.java)
                })
                .build()
    }

    fun loadData() {
        presenter.getBook()
    }

}