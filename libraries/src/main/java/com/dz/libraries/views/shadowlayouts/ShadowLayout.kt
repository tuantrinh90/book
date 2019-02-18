package com.dz.libraries.views.shadowlayouts

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.dz.libraries.R

class ShadowLayout : FrameLayout {
    var shadowColor: Int = 0
    var shadowRadius: Float = 0F
    var cornerRadius: Float = 0F
    var dx: Float = 0F
    var dy: Float = 0F

    var invalidateShadowOnSizeChanged = true
    var forceInvalidateShadow = false

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    override fun getSuggestedMinimumWidth(): Int = 0

    override fun getSuggestedMinimumHeight(): Int = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0 && (background == null || invalidateShadowOnSizeChanged || forceInvalidateShadow)) {
            forceInvalidateShadow = false
            setBackgroundCompat(w, h)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (forceInvalidateShadow) {
            forceInvalidateShadow = false
            setBackgroundCompat(right - left, bottom - top)
        }
    }

    fun invalidateShadow() {
        forceInvalidateShadow = true
        requestLayout()
        invalidate()
    }

    fun initView(context: Context, attrs: AttributeSet?) {
        initAttributes(context, attrs)
        val xPadding = (shadowRadius + Math.abs(dx)).toInt()
        val yPadding = (shadowRadius + Math.abs(dy)).toInt()
        setPadding(xPadding, yPadding, xPadding, yPadding)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("ObsoleteSdkInt")
    fun setBackgroundCompat(w: Int, h: Int) {
        val bitmap = createShadowBitmap(w, h, cornerRadius, shadowRadius, dx, dy, shadowColor, Color.TRANSPARENT)
        val drawable = BitmapDrawable(resources, bitmap)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(drawable)
        } else {
            background = drawable
        }
    }

    fun initAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = getTypedArray(context, attrs, R.styleable.ShadowLayout)
        try {
            dx = typedArray.getDimension(R.styleable.ShadowLayout_shadowLayoutDx, 0f)
            dy = typedArray.getDimension(R.styleable.ShadowLayout_shadowLayoutDy, 0f)
            cornerRadius = typedArray.getDimension(R.styleable.ShadowLayout_shadowLayoutCornerRadius, resources.getDimension(R.dimen.shadow_layout_default_corner_radius))
            shadowRadius = typedArray.getDimension(R.styleable.ShadowLayout_shadowLayoutShadowRadius, resources.getDimension(R.dimen.shadow_layout_default_shadow_radius))
            shadowColor = typedArray.getColor(R.styleable.ShadowLayout_shadowLayoutShadowColor, ContextCompat.getColor(context, R.color.shadow_layout_default_shadow_color))
        } finally {
            typedArray.recycle()
        }
    }

    fun getTypedArray(context: Context, attributeSet: AttributeSet?, attr: IntArray): TypedArray {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0)
    }

    fun createShadowBitmap(shadowWidth: Int, shadowHeight: Int, cornerRadius: Float, shadowRadius: Float,
                           dx: Float, dy: Float, shadowColor: Int, fillColor: Int): Bitmap {

        val output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ALPHA_8)
        val canvas = Canvas(output)

        val shadowRect = RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius)

        if (dy > 0) {
            shadowRect.top += dy
            shadowRect.bottom -= dy
        } else if (dy < 0) {
            shadowRect.top += Math.abs(dy)
            shadowRect.bottom -= Math.abs(dy)
        }

        if (dx > 0) {
            shadowRect.left += dx
            shadowRect.right -= dx
        } else if (dx < 0) {
            shadowRect.left += Math.abs(dx)
            shadowRect.right -= Math.abs(dx)
        }

        val shadowPaint = Paint()
        shadowPaint.isAntiAlias = true
        shadowPaint.color = fillColor
        shadowPaint.style = Paint.Style.FILL

        if (!isInEditMode) {
            shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor)
        }

        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint)

        return output
    }

}