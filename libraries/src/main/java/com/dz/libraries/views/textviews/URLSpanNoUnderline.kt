package com.dz.libraries.views.textviews

import android.text.TextPaint
import android.text.style.URLSpan

class URLSpanNoUnderline(url: String, private val linkColor: Int) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
        ds.color = linkColor
    }
}