package com.dz.listeners

import android.view.GestureDetector
import android.view.MotionEvent

class GestureCallback(val onSingleTapUp: () -> Unit, val onDoubleTapEvent: () -> Unit) : GestureDetector.SimpleOnGestureListener() {
    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        onSingleTapUp()
        return super.onSingleTapConfirmed(e)
    }


    override fun onDoubleTap(e: MotionEvent?): Boolean {
        onDoubleTapEvent()
        return super.onDoubleTap(e)
    }
}