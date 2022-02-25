package dev.barabu.morph.button

import androidx.annotation.DrawableRes

interface MorphStateController {

    fun morphToProgress(
        color: Int,
        progressPrimaryColor: Int,
        progressSecondaryColor: Int,
        progressCornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
    )

    fun morphToFinish(
        result: OpResult,
        colorNormal: Int,
        colorPressed: Int,
        cornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
        @DrawableRes iconId: Int
    )
}