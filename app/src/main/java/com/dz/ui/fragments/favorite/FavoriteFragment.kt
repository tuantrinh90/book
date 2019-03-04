package com.dz.ui.fragments.history

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.dz.commons.activities.alonefragment.AloneFragmentActivity
import com.dz.customizes.views.edittexts.EditTextApp
import com.dz.models.database.Book
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import com.dz.ui.fragments.history.adapter.HistoryAdapter
import io.reactivex.disposables.CompositeDisposable

class FavoriteFragment : BaseMainFragment<IFavoriteView, IFavoritePresenter>(), IFavoriteView {

    @BindView(R.id.list_book)
    lateinit var rvResult: RecyclerView
    @BindView(R.id.etSearch)
    lateinit var etSearch: EditTextApp
    private var newTextView: String? = ""
    var historyAdapter: HistoryAdapter? = null

    override fun createPresenter(): IFavoritePresenter = FavoritePresenter(appComponent)

    override val resourceId: Int get() = R.layout.favorite_fragment

    override val titleId: Int get() = R.string.favorite


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        initView()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun loadData() {
        presenter.getBook()
    }

    override fun setData(response: List<Book?>?) {
        historyAdapter?.setItems(response as ArrayList<Book?>)
    }

    fun initView() {
        rvResult.layoutManager = GridLayoutManager(context, 2)
        historyAdapter = HistoryAdapter(mActivity, null) {
            historyAdapter?.addItem(it)
            AloneFragmentActivity.with(this).parameters(DetailFragment.newBundle(it!!.id)).start(DetailFragment::class.java)
        }
        rvResult.adapter = historyAdapter

    }

}