package com.dz.libraries.views.buttons

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.Button
import com.dz.libraries.R
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.utilities.TypefaceUtility

class ExtButton : Button {
    constructor(context: Context) : super(context) {
        applyAttributes(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        applyAttributes(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        applyAttributes(context, attrs)
    }

    /**
     * @param context
     * @param attrs
     */
    private fun applyAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtButton)
        var fontPath = typedArray.getString(R.styleable.ExtButton_buttonFontAssetName)
        StringUtility.with(fontPath).doIfEmpty { fontPath = TypefaceUtility.FONT_DEFAULT }
        typeface = TypefaceUtility.with(context).get(fontPath!!)
        paintFlags = paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        typedArray.recycle()
    }
}
