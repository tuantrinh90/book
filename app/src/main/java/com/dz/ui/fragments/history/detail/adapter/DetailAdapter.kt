package com.dz.ui.fragments.history.detail.adapter

import android.content.Context
import android.view.View
import butterknife.BindView
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewAdapter
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.models.BookDetail
import com.dz.ui.R

class DetailAdapter(ctx: Context, its: ArrayList<BookDetail?>?, var itemConsumer: (BookDetail?) -> Unit) : ExtRecyclerViewAdapter<BookDetail, DetailAdapter.ViewHolder>(ctx, its) {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_history_detail

    override fun onCreateHolder(view: View, viewType: Int): DetailAdapter.ViewHolder = ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, data: BookDetail?, position: Int) {
        holder.tvTitle.text = data?.title
        holder.numberHistory.text = (position + 1).toString()
        holder.itemView.setOnClickListener { itemConsumer(data) }
    }

    class ViewHolder(itemView: View) : ExtRecyclerViewHolder(itemView) {
        @BindView(R.id.tvTitle)
        lateinit var tvTitle: ExtTextView
        @BindView(R.id.numberHistory)
        lateinit var numberHistory: ExtTextView

    }
}
