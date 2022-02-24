package dev.barabu.morph.button

import androidx.annotation.DrawableRes

interface MorphStateController {

    enum class State {
        Progress,
        Success,
        Error
    }

    fun morphToProgress(
        color: Int,
        progressPrimaryColor: Int,
        progressSecondaryColor: Int,
        progressCornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
    )

    fun morphToState(
        state: State,
        colorNormal: Int,
        colorPressed: Int,
        cornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
        @DrawableRes iconId: Int
    )
}