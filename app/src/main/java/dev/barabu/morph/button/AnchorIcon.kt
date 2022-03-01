package dev.barabu.morph.button

import androidx.annotation.DrawableRes

data class AnchorIcon(
    @DrawableRes val l: Int = 0,
    @DrawableRes val t: Int = 0,
    @DrawableRes val r: Int = 0,
    @DrawableRes val b: Int = 0
)

val AnchorIcon.resId: Int
    get() = listOf(l, t, r, b).firstOrNull { it != 0 } ?: 0