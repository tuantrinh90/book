package com.dz.libraries.views.recyclerviews


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dz.libraries.R
import com.dz.libraries.models.IModel
import com.dz.libraries.utilities.CollectionUtility
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.views.textviews.ExtTextView

class ExtRecyclerView<T : IModel, VH : ExtRecyclerViewHolder> : LinearLayout {
    companion object {
        var numberPerPage: Int = 0
    }

    // view
    private lateinit var swSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var rvRecyclerView: RecyclerView
    private lateinit var tvMessage: ExtTextView

    private var mAdapter: ExtRecyclerViewAdapter<T, VH>? = null
    private lateinit var endlessScrollListener: ExtRecyclerViewEndlessScrollListener

    @LayoutRes
    private var loadingLayout: Int = 0

    // listener
    private var loadMoreConsumer: (() -> Unit)? = null
    private var refreshConsumer: (() -> Unit)? = null

    // layout manger
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var hasFixedSize: Boolean = false

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.paging_recyclerview, this)

        // default layout manager
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        // refresh view
        swSwipeRefreshLayout = view.findViewById(R.id.swSwipeRefreshLayout)

        // rvRecyclerView
        rvRecyclerView = view.findViewById(R.id.rvRecyclerView)

        //data not found
        tvMessage = view.findViewById(R.id.tvMessage)
        tvMessage.visibility = View.GONE

        //attribute config
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtRecyclerView)

        // swipe refresh indicator color
        swSwipeRefreshLayout.setColorSchemeColors(typedArray.getColor(R.styleable.ExtRecyclerView_rvSwipeRefreshIndicatorColor,
                ContextCompat.getColor(context, R.color.colorAccent)))

        // scroll bar visibility
        rvRecyclerView.isVerticalScrollBarEnabled = typedArray.getBoolean(R.styleable.ExtRecyclerView_rvScrollbarVisible, false)

        // message no data
        StringUtility.with(typedArray.getString(R.styleable.ExtRecyclerView_rvNoDataMessage)).doIfPresent { tvMessage.text = it }
        typedArray.recycle()
    }

    /**
     * @return
     */
    val adapter: ExtRecyclerViewAdapter<T, VH>
        get() {
            if (mAdapter == null) throw NullPointerException()
            return mAdapter as ExtRecyclerViewAdapter<T, VH>
        }

    val itemCount: Int
        get() {
            if (mAdapter == null) throw NullPointerException()
            return if (mAdapter!!.isLoading) mAdapter!!.itemCount - 1 else mAdapter!!.itemCount
        }

    /**
     * set items
     */
    var items: ArrayList<T?>?
        get() {
            if (mAdapter == null) throw NullPointerException()
            return mAdapter!!.getItems()
        }
        set(its) {
            if (mAdapter == null) throw NullPointerException()
            mAdapter!!.setItems(its)
            hideLoading()
            displayMessage()
        }

    /**
     * @param adapter
     * @return
     */
    fun setAdapter(adapter: ExtRecyclerViewAdapter<T, VH>): ExtRecyclerView<T, VH> {
        this.mAdapter = adapter
        return this
    }

    /**
     * @param hasFixedSize
     * @return
     */
    fun hasFixedSize(hasFixedSize: Boolean): ExtRecyclerView<T, VH> {
        this.hasFixedSize = hasFixedSize
        return this
    }

    /**
     * @param layoutManager
     * @return
     */
    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager): ExtRecyclerView<T, VH> {
        this.layoutManager = layoutManager
        return this
    }

    /**
     * @param loadMoreConsumer
     * @return
     */
    fun setLoadMoreListener(loadMoreConsumer: (() -> Unit)? = null): ExtRecyclerView<T, VH> {
        this.loadMoreConsumer = loadMoreConsumer
        return this
    }

    /**
     * @param refreshConsumer
     * @return
     */
    fun setRefreshListener(refreshConsumer: (() -> Unit)? = null): ExtRecyclerView<T, VH> {
        this.refreshConsumer = refreshConsumer
        return this
    }

    /**
     * @param loadingLayout
     * @return
     */
    fun setLoadingLayout(@LayoutRes loadingLayout: Int): ExtRecyclerView<T, VH> {
        this.loadingLayout = loadingLayout
        return this
    }

    /**
     * build recycler view
     */
    fun build() {
        if (mAdapter == null) throw NullPointerException()

        // refresh layout
        swSwipeRefreshLayout.isEnabled = refreshConsumer != null
        swSwipeRefreshLayout.setOnRefreshListener {
            clear()
            swSwipeRefreshLayout.isRefreshing = false
            refreshConsumer?.invoke()
        }

        // number per page
        if (numberPerPage != 0) rvRecyclerView.setItemViewCacheSize(numberPerPage)

        // adapter
        mAdapter!!.setLoadingLayout(loadingLayout)
        rvRecyclerView.layoutManager = layoutManager
        rvRecyclerView.setHasFixedSize(hasFixedSize)
        rvRecyclerView.adapter = mAdapter

        // update load more
        endlessScrollListener = object : ExtRecyclerViewEndlessScrollListener(rvRecyclerView.layoutManager!!) {
            override fun onLoadMore(page: Int) {
                loadMoreConsumer?.invoke()
            }
        }
        rvRecyclerView.addOnScrollListener(endlessScrollListener)
    }

    /**
     * display message
     */
    private fun displayMessage() {
        tvMessage.visibility = if (mAdapter == null || mAdapter!!.itemCount <= 0) View.VISIBLE else View.GONE
    }

    /**
     * show loading
     */
    fun showLoading() {
        if (mAdapter == null) throw NullPointerException()
        mAdapter!!.showLoading()
        tvMessage.visibility = View.GONE
        endlessScrollListener.isLoading = true
    }

    /**
     * hide loading
     */
    fun hideLoading() {
        if (mAdapter == null) throw NullPointerException()
        mAdapter!!.hideLoading()
        endlessScrollListener.isLoading = false
    }

    /**
     * clear data
     */
    fun clear() {
        if (mAdapter == null) throw NullPointerException()
        tvMessage.visibility = View.GONE
        endlessScrollListener.resetState()
        mAdapter!!.clear()
    }

    /**
     * @param position
     * @return
     */
    fun getItem(position: Int): T? {
        if (mAdapter == null) throw NullPointerException()
        return mAdapter!!.getItem(position)
    }

    /**
     * @param item
     */
    fun addItem(item: T?) {
        if (mAdapter == null) throw NullPointerException()
        mAdapter!!.addItem(item)
    }

    /**
     * @param index
     * @param item
     */
    fun addItem(index: Int, item: T?) {
        if (mAdapter == null) throw NullPointerException()
        mAdapter!!.addItem(index, item)
    }

    /**
     * @param items
     */
    fun addItems(items: ArrayList<T?>?) {
        if (mAdapter == null) throw NullPointerException()
        if (CollectionUtility.isNullOrEmpty(items) || CollectionUtility.with(items).size() < numberPerPage) {
            endlessScrollListener.lastPage = true
        }

        hideLoading()
        mAdapter!!.addItems(items)
        displayMessage()
    }

    /**
     * @param index
     * @param item
     */
    fun update(index: Int, item: T?) {
        if (mAdapter == null) throw NullPointerException()
        mAdapter!!.update(index, item)
    }

    /**
     * @param item
     */
    fun removeItem(item: T?) {
        if (mAdapter == null) throw NullPointerException()
        mAdapter!!.removeItem(item)
        displayMessage()
    }

    /**
     * @param index
     */
    fun removeItem(index: Int) {
        if (mAdapter == null) throw NullPointerException()
        if (index != -1) mAdapter!!.removeItem(index)
        displayMessage()
    }

    /**
     * update recycler view
     */
    fun notifyDataSetChanged() {
        if (mAdapter == null) throw NullPointerException()
        mAdapter!!.notifyDataSetChanged()
        displayMessage()
    }
}