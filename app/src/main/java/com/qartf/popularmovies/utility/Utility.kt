package com.qartf.popularmovies.utility

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.KeyEvent
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.qartf.popularmovies.R
import kotlin.math.hypot

class Utility {
    companion object {
        fun resize(x: Int, y: Int, image: Drawable, res: Resources): Drawable {
            return if (image is BitmapDrawable && x > 0 && y > 0) {
                val b = image.bitmap
                val bitmapResized = Bitmap.createScaledBitmap(b, x, y, false)
                BitmapDrawable(res, bitmapResized)
            } else image
        }

        fun circularRevealIn(view: View, time: Long) {
            val cx = view.width / 2
            val cy = view.height / 2
            val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
            anim.duration = time
            anim.start()
        }

        fun circularRevealOut(view: View, time: Long, doOnEnd: () -> Unit) {
            val cx = view.width / 2
            val cy = view.height / 2
            val initialRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0f)
            anim.doOnEnd { doOnEnd.invoke() }
            anim.duration = time
            anim.start()
        }

        fun onCreateDialog(context: Context, dialog: Dialog, rootView: View, timeIn: Long, timeOut: Long) {
            dialog.setCanceledOnTouchOutside(false)
            dialog.window?.decorView?.background =
                ColorDrawable(ContextCompat.getColor(context, R.color.transparent))

            val dialogBounds = Rect()
            var circularRevealIsOff = true
            dialog.window!!.decorView.setOnTouchListener { _, motionEvent ->
                if (circularRevealIsOff) {
                    dialog.window!!.decorView.getHitRect(dialogBounds)
                    if (dialogBounds.contains(motionEvent.x.toInt(), motionEvent.y.toInt()).not()) {
                        circularRevealOut(rootView, timeOut) { dialog.dismiss() }
                        circularRevealIsOff = false
                    }
                }
                return@setOnTouchListener false
            }

            dialog.setOnKeyListener { _, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                    circularRevealOut(rootView, timeOut) { dialog.dismiss() }
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

            dialog.setOnShowListener {
                circularRevealIn(rootView, timeIn)
            }
        }
    }
}