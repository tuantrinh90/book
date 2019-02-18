package com.dz.customizes.views.footers

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.ui.R

class FooterItem @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    @BindView(R.id.llView)
    lateinit var llView: LinearLayout
    @BindView(R.id.ivIcon)
    lateinit var ivIcon: ImageView
    @BindView(R.id.tvContent)
    lateinit var tvContent: ExtTextView

    // icon
    var icon: Drawable? = null

    init {
        initView(context, attrs)
    }

    /**
     * @param context
     * @param attrs
     */
    fun initView(context: Context, attrs: AttributeSet?) {
        val view = LayoutInflater.from(context).inflate(R.layout.footer_item, this)
        ButterKnife.bind(this, view)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FooterItem)

        // icon
        icon = typedArray.getDrawable(R.styleable.FooterItem_footerItemIcon)
        ivIcon.setImageDrawable(icon)

        // content
        tvContent.text = typedArray.getString(R.styleable.FooterItem_footerItemContent)

        typedArray.recycle()
    }

    /**
     * @param context
     * @param isActive
     */
    fun setActiveMode(context: Context, isActive: Boolean) {
        // icon
        icon?.setColorFilter(ContextCompat.getColor(context, if (isActive) R.color.color_pink else R.color.color_border), PorterDuff.Mode.SRC_ATOP)
        ivIcon.setImageDrawable(icon)

        // text
        tvContent.setTextColor(ContextCompat.getColor(context, if (isActive) R.color.color_pink else R.color.color_border))
    }
}
