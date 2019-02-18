package com.dz.customizes.views.linearlayouts

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.LinearLayout
import com.dz.listeners.GestureCallback

class ExtLinearLayout(context: Context,
                      onSingleTapUp: () -> Unit,
                      onDoubleTapEvent: () -> Unit,
                      attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    var gestureDetector: GestureDetector = GestureDetector(context, GestureCallback(onSingleTapUp, onDoubleTapEvent))

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
}