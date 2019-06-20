package com.qartf.popularmovies.utility

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

class Utility {
    companion object {
        fun resize(x: Int, y: Int, image: Drawable, res: Resources): Drawable {
            return if (image is BitmapDrawable && x > 0 && y > 0) {
                val b = image.bitmap
                val bitmapResized = Bitmap.createScaledBitmap(b, x, y, false)
                BitmapDrawable(res, bitmapResized)
            } else image
        }
    }
}