package com.dz.libraries.views.viewpagers

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.dz.libraries.loggers.Logger

class ExtViewPager : ViewPager {
    companion object {
        private const val TAG = "ExtViewPager"
    }

    private var initialXValue: Float = 0f
    private var direction = ExtViewPagerSwipeDirection.ALL

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (isSwipeAllowed(event)) super.onTouchEvent(event) else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (isSwipeAllowed(event)) super.onInterceptTouchEvent(event) else false
    }

    private fun isSwipeAllowed(event: MotionEvent): Boolean {
        if (direction == ExtViewPagerSwipeDirection.ALL) return true
        if (direction == ExtViewPagerSwipeDirection.NONE) return false

        if (event.action == MotionEvent.ACTION_DOWN) {
            initialXValue = event.x
            return true
        }

        if (event.action == MotionEvent.ACTION_MOVE) {
            try {
                val diffX = event.x - initialXValue
                if (diffX > 0 && direction == ExtViewPagerSwipeDirection.RIGHT) {
                    return false // swipe from left to right detected
                } else if (diffX < 0 && direction == ExtViewPagerSwipeDirection.LEFT) {
                    return false // swipe from right to left detected
                }
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }
        }

        return true
    }

    /**
     * @param direction
     */
    fun setAllowedSwipeDirection(direction: ExtViewPagerSwipeDirection): ExtViewPager {
        this.direction = direction
        return this
    }

    enum class ExtViewPagerSwipeDirection { ALL, LEFT, RIGHT, NONE }
}