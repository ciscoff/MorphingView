package dev.barabu.morph

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

open class BaseActivity : AppCompatActivity() {

    fun dimen(@DimenRes resId: Int): Float = resources.getDimension(resId)
    fun color(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)
    fun integer(@IntegerRes resId: Int): Int = resources.getInteger(resId)
}