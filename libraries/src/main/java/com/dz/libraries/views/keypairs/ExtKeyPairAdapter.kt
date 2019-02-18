package com.dz.libraries.views.keypairs

import android.content.Context
import androidx.core.widget.TextViewCompat
import android.view.Gravity
import android.view.View
import com.dz.libraries.R
import com.dz.libraries.utilities.OptionalUtility
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewAdapter
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.libraries.views.textviews.ExtTextView

class ExtKeyPairAdapter(context: Context, items: ArrayList<ExtKeyPair?>?,
                        var itemConsumer: ((value: ExtKeyPair) -> Unit)? = null) : ExtRecyclerViewAdapter<ExtKeyPair, ExtKeyPairAdapter.ViewHolder>(context, items) {

    var textGravity = Gravity.CENTER

    /**
     * @param viewType
     * @return
     */
    override fun getLayoutId(viewType: Int): Int = R.layout.key_pair_row

    /**
     * @param view
     * @param viewType
     * @return
     */
    override fun onCreateHolder(view: View, viewType: Int): ViewHolder = ViewHolder(view)

    /**
     * @param holder
     * @param data
     * @param position
     */
    override fun onBindViewHolder(holder: ViewHolder, data: ExtKeyPair?, position: Int) {
        holder.setData(data, textGravity)
        holder.itemView.setOnClickListener { itemConsumer?.invoke(data!!) }
    }

    class ViewHolder(view: View) : ExtRecyclerViewHolder(view) {
        private val tvContent: ExtTextView = view.findViewById(R.id.tvContent)

        fun setData(keyPair: ExtKeyPair?, textGravity: Int) {
            if (OptionalUtility.isNullOrEmpty(keyPair)) return

            with(keyPair!!) {
                tvContent.text = value
                tvContent.gravity = textGravity
                TextViewCompat.setTextAppearance(tvContent, if (selected) R.style.StyleNormalBold else R.style.StyleNormal)
            }
        }
    }
}
