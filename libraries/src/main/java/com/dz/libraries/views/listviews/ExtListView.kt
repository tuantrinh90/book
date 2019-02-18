package com.dz.libraries.views.listviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import com.dz.libraries.loggers.Logger

class ExtListView : ListView {
    companion object {
        private const val TAG = "ExtListView"
    }

    /**
     * @param context
     */
    constructor(context: Context) : super(context)

    /**
     * @param context
     * @param attrs
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        try {
            val heightSpec: Int = if (layoutParams.height == AbsListView.LayoutParams.WRAP_CONTENT) {
                View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 1, View.MeasureSpec.AT_MOST)
            } else {
                heightMeasureSpec  // Any other height should be respected as is.
            }
            super.onMeasure(widthMeasureSpec, heightSpec)
        } catch (ex: Exception) {
            Logger.e(TAG, ex)
        }
    }
}