package com.dz.ui.fragments.friends.addfriends.adapters

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import butterknife.BindView
import com.dz.customizes.functions.images.transform.CircleTransform
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewAdapter
import com.dz.libraries.views.recyclerviews.ExtRecyclerViewHolder
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.models.responses.MemberResponse
import com.dz.ui.R
import com.dz.utilities.Constant
import com.dz.utilities.PicasoUtility

class AddFriendAdapter(
        ctx: Context, its: ArrayList<MemberResponse?>?, var isViewMode: Boolean,
        var itemConsumer: (MemberResponse?) -> Unit,
        var removeConsumer: (MemberResponse?) -> Unit
) : ExtRecyclerViewAdapter<MemberResponse, AddFriendAdapter.ViewHolder>(ctx, its) {
    override fun getLayoutId(viewType: Int): Int = R.layout.add_friend_item

    override fun onCreateHolder(view: View, viewType: Int): ViewHolder = ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, data: MemberResponse?, position: Int) {
        PicasoUtility.get()
                .load(PicasoUtility.getLink(data?.photo))
                .placeholder(R.drawable.bg_background_radius_component)
                .transform(CircleTransform(borderWidth = Constant.BORDER_IMAGE))
                .error(R.drawable.bg_avatar_default)
                .into(holder.ivAvatar)

        holder.tvName.text = data?.fullNameText

        holder.ivRemove.visibility = if (isViewMode) View.VISIBLE else View.GONE
        holder.ivRemove.setOnClickListener { removeConsumer(data) }

        holder.cbCheckBox.isChecked = data?.isChecked ?: false
        holder.cbCheckBox.visibility = if (isViewMode) View.GONE else View.VISIBLE
        holder.cbCheckBox.setOnCheckedChangeListener { _, isChecked -> data?.isChecked = isChecked }

        holder.itemView.setOnClickListener {
            data?.isChecked = !(data?.isChecked ?: false)
            holder.cbCheckBox.isChecked = (data?.isChecked ?: false)
            itemConsumer(data)
        }
    }

    class ViewHolder(itemView: View) : ExtRecyclerViewHolder(itemView) {
        @BindView(R.id.ivAvatar)
        lateinit var ivAvatar: ImageView
        @BindView(R.id.tvName)
        lateinit var tvName: ExtTextView
        @BindView(R.id.ivRemove)
        lateinit var ivRemove: ImageView
        @BindView(R.id.cbCheckBox)
        lateinit var cbCheckBox: CheckBox
    }
}