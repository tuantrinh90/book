package com.dz.libraries.views.numberpickers

import android.content.Context
import androidx.core.widget.TextViewCompat
import android.util.AttributeSet
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import com.dz.libraries.R
import com.dz.libraries.utilities.FontUtility
import com.dz.libraries.utilities.TypefaceUtility

class ExtNumberPicker : NumberPicker {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun addView(child: View) {
        super.addView(child)
        updateView(child)
    }

    override fun addView(child: View, index: Int, params: android.view.ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        updateView(child)
    }

    override fun addView(child: View, params: android.view.ViewGroup.LayoutParams) {
        super.addView(child, params)
        updateView(child)
    }

    private fun updateView(view: View) {
        if (view is TextView) {
            FontUtility.setCustomizeTypeFace(context, view, TypefaceUtility.FONT_DEFAULT)
            TextViewCompat.setTextAppearance(view, R.style.StyleNormal)
        }
    }
}
