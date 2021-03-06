package com.dz.ui.fragments.history.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewAdapter
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.models.database.Book
import com.dz.ui.R
import com.dz.utilities.PicasoUtility

class HistoryAdapter(ctx: Context, its: ArrayList<Book?>?, var itemConsumer: (Book?) -> Unit) : ExtRecyclerViewAdapter<Book, HistoryAdapter.ViewHolder>(ctx, its) {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_history

    override fun onCreateHolder(view: View, viewType: Int): ViewHolder = ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, data: Book?, position: Int) {
        holder.tvAuthor.text = data?.author
        holder.tvNameBook.text = data?.name
        PicasoUtility.get()
                .load(data!!.image)
                .placeholder(R.drawable.bg_background_radius_component)
                .into(holder.ivContent)
        holder.itemView.setOnClickListener { itemConsumer(data) }
    }

    class ViewHolder(itemView: View) : ExtRecyclerViewHolder(itemView) {
        @BindView(R.id.tvAuthor)
        lateinit var tvAuthor: ExtTextView
        @BindView(R.id.tvNameBook)
        lateinit var tvNameBook: ExtTextView
        @BindView(R.id.ivContent)
        lateinit var ivContent: ImageView

    }
}
