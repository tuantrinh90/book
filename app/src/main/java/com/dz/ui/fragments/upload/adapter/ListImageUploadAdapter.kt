package com.dz.ui.fragments.upload.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewAdapter
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.models.responses.UploadResponse
import com.dz.ui.R
import com.dz.utilities.PicasoUtility
import com.squareup.picasso.Callback

class ListImageUploadAdapter(
        ctx: Context, val its: ArrayList<UploadResponse?>,
        var itemConsumer: (UploadResponse?) -> Unit,
        var removeConsumer: (UploadResponse?) -> Unit
) : ExtRecyclerViewAdapter<UploadResponse, ListImageUploadAdapter.ViewHolder>(ctx, its) {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_image_upload

    override fun onCreateHolder(view: View, viewType: Int): ListImageUploadAdapter.ViewHolder = ViewHolder(view)

    override fun onBindViewHolder(holder: ListImageUploadAdapter.ViewHolder, data: UploadResponse?, position: Int) {
        if (data == null) return

        StringUtility.with(data.file)
                .doIfEmpty {
                    holder.ivAdd.visibility = View.VISIBLE
                    holder.ivClear.visibility = View.GONE

                    holder.ivAdd.setImageResource(R.drawable.ic_add_circle_blue)
                    holder.ivImage.setImageDrawable(null)
                }
                .doIfPresent {
                    holder.ivAdd.visibility = View.GONE
                    holder.ivClear.visibility = View.VISIBLE

                    PicasoUtility.get()
                            .load(PicasoUtility.getLink(it))
                            .placeholder(R.drawable.bg_background_radius_component)
                            .into(holder.ivImage, object : Callback.EmptyCallback() {
                                override fun onError(e: Exception?) {
                                    Log.e("Picaso", "Picaso: $e")
                                }
                            })
                }

        holder.ivClear.setOnClickListener { removeConsumer(data) }
        holder.ivImage.setOnClickListener { itemConsumer(data) }
    }

    class ViewHolder(itemView: View) : ExtRecyclerViewHolder(itemView) {
        @BindView(R.id.ivImage)
        lateinit var ivImage: ImageView
        @BindView(R.id.ivAdd)
        lateinit var ivAdd: ImageView
        @BindView(R.id.ivClear)
        lateinit var ivClear: ImageView
    }
}