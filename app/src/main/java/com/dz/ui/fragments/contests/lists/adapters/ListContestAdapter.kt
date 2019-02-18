package com.dz.ui.fragments.contests.lists.adapters

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewAdapter
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.models.responses.ContestResponse
import com.dz.ui.R
import com.dz.utilities.Constant
import com.dz.utilities.PicasoUtility

class ListContestAdapter(ctx: Context, its: ArrayList<ContestResponse?>?,
                         var joinConsumer: (ContestResponse?) -> Unit,
                         var itemConsumer: (ContestResponse?) -> Unit) : ExtRecyclerViewAdapter<ContestResponse, ListContestAdapter.ViewHolder>(ctx, its) {
    override fun getLayoutId(viewType: Int): Int = R.layout.list_contest_item

    override fun onCreateHolder(view: View, viewType: Int): ViewHolder = ViewHolder(view)

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ViewHolder, data: ContestResponse?, position: Int) {
        PicasoUtility.get()
                .load(PicasoUtility.getLink(data?.filePath))
                .placeholder(R.drawable.bg_background_radius_component)
                .into(holder.ivMedia)

        holder.tvDescription.text = Html.fromHtml(data?.name)
        holder.tvType.text = context.getString(R.string.type_row)
                .format(if (data?.fileType == Constant.TYPE_IMAGE) context.getString(R.string.image) else context.getString(R.string.video))
        holder.tvDate.text = context.getString(R.string.from_to).format(data?.startDateText, data?.endDateText)
        holder.tvSubmission.text = context.getString(R.string.submissions).format("${data?.numberOfParticipants}")
        holder.tvJoin.visibility = if (data?.status == Constant.OPEN) View.VISIBLE else View.GONE
        holder.tvJoin.setOnClickListener { joinConsumer(data) }
        holder.itemView.setOnClickListener { itemConsumer(data) }
    }

    class ViewHolder(itemView: View) : ExtRecyclerViewHolder(itemView) {
        @BindView(R.id.ivMedia)
        lateinit var ivMedia: ImageView
        @BindView(R.id.tvDescription)
        lateinit var tvDescription: ExtTextView
        @BindView(R.id.tvType)
        lateinit var tvType: ExtTextView
        @BindView(R.id.tvDate)
        lateinit var tvDate: ExtTextView
        @BindView(R.id.tvSubmission)
        lateinit var tvSubmission: ExtTextView
        @BindView(R.id.tvJoin)
        lateinit var tvJoin: ExtTextView
    }
}