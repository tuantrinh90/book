package com.dz.ui.fragments.history

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.dz.commons.activities.alonefragment.AloneFragmentActivity
import com.dz.models.responses.BookResponse
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import com.dz.ui.fragments.history.adapter.HistoryAdapter
import com.dz.ui.fragments.history.detail.adapter.DetailAdapter
import io.reactivex.disposables.CompositeDisposable

class DetailFragment : BaseMainFragment<IDetailView, IDetailPresenter>(), IDetailView {


    @BindView(R.id.rvLinks)
    lateinit var rVLinks: RecyclerView
    var detailAdapter: DetailAdapter? = null

    override fun createPresenter(): IDetailPresenter = DetailPresenter(appComponent)

    override val resourceId: Int get() = R.layout.history_detail_fragment

    override val titleId: Int get() = R.string.home


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        initView()
    }

    fun initView() {
        detailAdapter = DetailAdapter(mActivity, null) {
            detailAdapter?.addItem(it)
        }
        rVLinks.adapter = detailAdapter
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

}