package com.dz.ui.fragments.hashtags.adapters

import android.content.Context
import android.view.View
import butterknife.BindView
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewAdapter
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.models.responses.HashTagResponse
import com.dz.ui.R

class ResultAdapter(ctx: Context, its: ArrayList<HashTagResponse?>?, var itemConsumer: (HashTagResponse?) -> Unit) : ExtRecyclerViewAdapter<HashTagResponse, ResultAdapter.ViewHolder>(ctx, its) {
    override fun getLayoutId(viewType: Int): Int = R.layout.hash_tag_item

    override fun onCreateHolder(view: View, viewType: Int): ViewHolder = ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, data: HashTagResponse?, position: Int) {
        holder.tvName.text = data?.name
        holder.itemView.setOnClickListener { itemConsumer(data) }
    }

    class ViewHolder(itemView: View) : ExtRecyclerViewHolder(itemView) {
        @BindView(R.id.tvName)
        lateinit var tvName: ExtTextView
    }
}