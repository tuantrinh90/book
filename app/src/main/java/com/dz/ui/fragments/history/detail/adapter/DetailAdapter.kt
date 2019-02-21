package com.dz.ui.fragments.history.detail.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewAdapter
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.models.responses.BookResponse
import com.dz.ui.R
import com.dz.utilities.PicasoUtility

class DetailAdapter(ctx: Context, its: ArrayList<BookResponse?>?, var itemConsumer: (BookResponse?) -> Unit) : ExtRecyclerViewAdapter<BookResponse, DetailAdapter.ViewHolder>(ctx, its) {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_history_detail

    override fun onCreateHolder(view: View, viewType: Int): ViewHolder = ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, data: BookResponse?, position: Int) {
        holder.tvTitle.text = data?.name
        PicasoUtility.get()
                .load(data!!.link)
                .placeholder(R.drawable.bg_background_radius_component)
                .into(holder.ivPlayVideo)
        holder.itemView.setOnClickListener { itemConsumer(data) }
    }

    class ViewHolder(itemView: View) : ExtRecyclerViewHolder(itemView) {
        @BindView(R.id.tvTitle)
        lateinit var tvTitle: ExtTextView
        @BindView(R.id.ivPlayVideo)
        lateinit var ivPlayVideo: ImageView

    }
}
