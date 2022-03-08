package dev.barabu.morph.extentions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

fun Context.dimen(@DimenRes resId: Int): Float = resources.getDimension(resId)
fun Context.color(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)
fun Context.integer(@IntegerRes resId: Int): Int = resources.getInteger(resId)
fun Context.string(@StringRes resId: Int): String = resources.getString(resId)