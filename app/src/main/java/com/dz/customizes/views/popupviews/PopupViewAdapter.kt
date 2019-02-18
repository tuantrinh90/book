package com.dz.customizes.views.popupviews

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import butterknife.BindView
import com.dz.libraries.views.keypairs.ExtKeyPair
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewAdapter
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.ui.R

class PopupViewAdapter(ctx: Context, its: ArrayList<ExtKeyPair?>? = null,
                       val value: ExtKeyPair? = null,
                       val consumer: ((ExtKeyPair?) -> Unit)? = null) : ExtRecyclerViewAdapter<ExtKeyPair, PopupViewAdapter.ViewHolder>(ctx, its) {
    override fun getLayoutId(viewType: Int): Int = R.layout.popup_item

    override fun onCreateHolder(view: View, viewType: Int): ViewHolder = ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, data: ExtKeyPair?, position: Int) {
        holder.tvContent.text = data?.value ?: ""
        holder.tvContent.setTextColor(ContextCompat.getColor(context,
                if (value?.key.equals(data?.key, true)) R.color.colorAccent else R.color.color_black))
//        holder.itemView.setBackgroundColor(ContextCompat.getColor(context,
//                if (value?.key.equals(data?.key, true)) R.color.colorPrimary else R.color.color_white))
        holder.itemView.setOnClickListener { consumer?.invoke(data) }
    }

    class ViewHolder(itemView: View) : ExtRecyclerViewHolder(itemView) {
        @BindView(R.id.tvContent)
        lateinit var tvContent: ExtTextView
    }
}