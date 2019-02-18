package com.dz.libraries.views.autocompletetextviews

import android.content.Context
import android.graphics.Paint
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import android.util.AttributeSet
import com.dz.libraries.R
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.utilities.TypefaceUtility

class ExtAutoCompleteTextView : AppCompatAutoCompleteTextView {
    constructor(context: Context) : super(context) {
        applyAttributes(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        applyAttributes(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        applyAttributes(context, attrs)
    }

    /**
     * @param context
     * @param attrs
     */
    private fun applyAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtAutoCompleteTextView)
        var fontPath = typedArray.getString(R.styleable.ExtAutoCompleteTextView_autoCompleteFontAssetName)
        StringUtility.with(fontPath).doIfEmpty { fontPath = TypefaceUtility.FONT_DEFAULT }
        typeface = TypefaceUtility.with(context).get(fontPath!!)
        paintFlags = paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        typedArray.recycle()
    }
}
