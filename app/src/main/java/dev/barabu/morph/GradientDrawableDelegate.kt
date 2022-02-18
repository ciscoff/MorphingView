package dev.barabu.morph

import android.graphics.Color
import android.graphics.drawable.GradientDrawable

class GradientDrawableDelegate(val gradientDrawable: GradientDrawable) {

    var strokeWidth: Int = 0
        set(value) {
            field = value
            gradientDrawable.setStroke(strokeWidth, strokeColor)
        }

    var strokeColor: Int = Color.TRANSPARENT
        set(value) {
            field = value
            gradientDrawable.setStroke(strokeWidth, strokeColor)
        }

    var color: Int = Color.TRANSPARENT
        set(value) {
            field = value
            gradientDrawable.setColor(value)
        }

    var cornerRadius: Float = 0f
        set(value) {
            field = value
            gradientDrawable.cornerRadius = value
        }

}