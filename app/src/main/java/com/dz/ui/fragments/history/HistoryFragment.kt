package com.dz.ui.fragments.history

import android.os.Bundle
import android.view.View
import butterknife.BindView
import com.dz.libraries.views.recyclerviews.ExtRecyclerView
import com.dz.models.responses.BookResponse
import com.dz.models.responses.HashTagResponse
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import com.dz.ui.fragments.history.adapter.HistoryAdapter
import io.reactivex.disposables.CompositeDisposable

class HistoryFragment : BaseMainFragment<IHistoryView, IHistoryPresenter>(), IHistoryView {

    enum class Type {
        OPEN, CLOSE
    }

    @BindView(R.id.list_book)
    lateinit var rvResult: ExtRecyclerView<BookResponse, HistoryAdapter.ViewHolder>

    var historyAdapter: HistoryAdapter? = null
    var books = ArrayList<BookResponse?>()

    override fun createPresenter(): IHistoryPresenter = HistoryPresenter(appComponent)

    override val resourceId: Int get() = R.layout.history_fragment

    override val titleId: Int get() = R.string.home

    override fun setData(response: ArrayList<BookResponse?>?) {
        rvResult.addItems(response)
    }

    var mType = HistoryFragment.Type.OPEN

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        initView()
        loadData()
    }

    fun initView() {
        rvResult
                .setAdapter(HistoryAdapter(mActivity, null) {
                    historyAdapter?.addItem(it)
                })
    }

    fun setCompositeDisposable(compositeDisposable: CompositeDisposable): HistoryFragment {
        return this
    }

    fun loadData() {
        presenter.getBook()
    }

    fun setType(type: HistoryFragment.Type): HistoryFragment {
        mType = type
        return this
    }

}