package com.dz.libraries.views.checkboxs

import android.content.Context
import android.graphics.Paint
import androidx.appcompat.widget.AppCompatCheckBox
import android.util.AttributeSet
import com.dz.libraries.R
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.utilities.TypefaceUtility

class ExtCheckBox : AppCompatCheckBox {

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
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtCheckBox)
        var fontPath = typedArray.getString(R.styleable.ExtCheckBox_checkBoxFontAssetName)
        StringUtility.with(fontPath).doIfEmpty { fontPath = TypefaceUtility.FONT_DEFAULT }
        typeface = TypefaceUtility.with(context).get(fontPath!!)
        paintFlags = paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        typedArray.recycle()
    }
}
