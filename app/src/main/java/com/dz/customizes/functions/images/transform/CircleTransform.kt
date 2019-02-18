package com.dz.customizes.functions.images.transform

import android.graphics.*
import com.squareup.picasso.Transformation

class CircleTransform(private val borderColor: Int = Color.LTGRAY, private val borderWidth: Int = 8) : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)
        Canvas(bitmap).apply {
            val r = size / 2f

            // Draw the background circle
            drawCircle(r, r, r, Paint().apply {
                color = borderColor
                isAntiAlias = true
            })

            // Draw the image smaller than the background so a little border will be seen
            drawCircle(r, r, r - borderWidth, Paint().apply {
                shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                isAntiAlias = true
            })
        }

        // recycle bitmap
        squaredBitmap.recycle()

        // bitmap result
        return bitmap
    }

    override fun key(): String {
        return "Circle"
    }
}