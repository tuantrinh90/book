package com.dz.libraries.views.edittexts

import android.content.Context
import android.graphics.Paint
import androidx.appcompat.widget.AppCompatEditText
import android.util.AttributeSet
import com.dz.libraries.R
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.utilities.TypefaceUtility

class ExtEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        applyAttributes(context, null)
    }

    /**
     * Constructor
     *
     * @param context
     * @param attrs
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        applyAttributes(context, attrs)
    }

    /**
     * Constructor
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        applyAttributes(context, attrs)
    }

    /**
     * Init control params
     *
     * @param context
     * @param attrs
     */
    private fun applyAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtEditText)
        // font
        var fontPath = typedArray.getString(R.styleable.ExtEditText_editTextFontAssetName)
        StringUtility.with(fontPath).doIfEmpty { fontPath = TypefaceUtility.FONT_DEFAULT }
        typeface = TypefaceUtility.with(context).get(fontPath!!)
        paintFlags = paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        typedArray.recycle()
    }
}
