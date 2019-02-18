package com.dz.ui.fragments.hashtags.adapters

import android.content.Context
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewAdapter
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.models.responses.HashTagResponse
import com.dz.ui.R

class HashTagAdapter(
        ctx: Context, its: ArrayList<HashTagResponse?>?, var removeConsumer: (HashTagResponse?) -> Unit
) : ExtRecyclerViewAdapter<HashTagResponse, HashTagAdapter.ViewHolder>(ctx, its) {
    override fun getLayoutId(viewType: Int): Int = R.layout.hash_tag_choose_item

    override fun onCreateHolder(view: View, viewType: Int): ViewHolder = ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, data: HashTagResponse?, position: Int) {
        holder.tvName.text = data?.name
        holder.ivRemove.setOnClickListener { removeConsumer(data) }
    }

    class ViewHolder(itemView: View) : ExtRecyclerViewHolder(itemView) {
        @BindView(R.id.tvName)
        lateinit var tvName: ExtTextView
        @BindView(R.id.ivRemove)
        lateinit var ivRemove: ImageView
    }
}