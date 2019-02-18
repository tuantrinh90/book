package com.dz.customizes.views.popupviews

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.dz.libraries.views.keypairs.ExtKeyPair
import com.dz.ui.R

class PopupView(context: Context?) : LinearLayout(context) {
    @BindView(R.id.rvRecyclerView)
    lateinit var rvRecyclerView: RecyclerView

    var value: ExtKeyPair? = null
    var its: ArrayList<ExtKeyPair?>? = null
    var popupViewAdapter: PopupViewAdapter? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.popup_view, this)
        ButterKnife.bind(this, view)
    }

    @Suppress("UNCHECKED_CAST")
    fun setData(value: ExtKeyPair? = null,
                its: ArrayList<ExtKeyPair?>? = null,
                consumer: ((ExtKeyPair?) -> Unit)? = null): PopupView {
        this.value = value
        this.its = its

        rvRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        popupViewAdapter = PopupViewAdapter(context, its, value, consumer)
        rvRecyclerView.adapter = popupViewAdapter

        // scroll to position has selected
        value?.let { rvRecyclerView.scrollToPosition(its?.indexOf(it) ?: 0) }
        return this
    }
}