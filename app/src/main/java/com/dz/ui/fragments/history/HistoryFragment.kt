package com.dz.ui.fragments.history

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.dz.commons.activities.alonefragment.AloneFragmentActivity
import com.dz.customizes.views.edittexts.EditTextApp
import com.dz.libraries.utilities.StringUtility
import com.dz.models.responses.BookResponse
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import com.dz.ui.fragments.history.adapter.HistoryAdapter

class HistoryFragment : BaseMainFragment<IHistoryView, IHistoryPresenter>(), IHistoryView {

    @BindView(R.id.list_book)
    lateinit var rvResult: RecyclerView
    @BindView(R.id.etSearch)
    lateinit var etSearch: EditTextApp
    private var newTextView: String? = ""
    var historyAdapter: HistoryAdapter? = null

    override fun createPresenter(): IHistoryPresenter = HistoryPresenter(appComponent)

    override val resourceId: Int get() = R.layout.history_fragment

    override val titleId: Int get() = R.string.home

    override fun setData(response: ArrayList<BookResponse?>?) {
        historyAdapter?.setItems(response)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        initView()
        loadData()
        eventSearch()
    }

    fun eventSearch() {
        // text change
        etSearch.textChangeConsumer = { text ->
            Log.e("text:::::", text)
            fillterSearch(text)
            newTextView = text
            etSearch.iconRightImageView.visibility = if (StringUtility.isNullOrEmpty(text)) View.INVISIBLE else View.VISIBLE
        }
        etSearch.iconRightImageView.visibility = View.INVISIBLE
        etSearch.iconRightImageView.setOnClickListener { etSearch.setContent("") }
        // edit text search listener
        etSearch.etContent.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                fillterSearch(newTextView!!)
                return@OnEditorActionListener true
            }
            false
        })
    }

    fun initView() {
        rvResult.layoutManager = GridLayoutManager(context, 2)
        historyAdapter = HistoryAdapter(mActivity, null) {
            historyAdapter?.addItem(it)
            AloneFragmentActivity.with(mActivity).start(DetailFragment::class.java)
        }
        rvResult.adapter = historyAdapter

    }

    fun loadData() {
        presenter.getBook()
    }

    fun fillterSearch(query: String) {
        if (StringUtility.isNullOrEmpty(query)) {
            historyAdapter?.getItems()!!.clear()
            loadData()
        } else {
            val listSearch = historyAdapter?.getItems()!!.filter { it -> it!!.author!!.contains(query) } as ArrayList<BookResponse?>
            Log.e("listSearch:::", listSearch.toString())
            historyAdapter?.setItems(listSearch)
        }

    }
}