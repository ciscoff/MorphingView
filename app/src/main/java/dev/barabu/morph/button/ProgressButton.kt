package dev.barabu.morph.button

interface ProgressButton {
    fun morphToProgress(
        color: Int,
        progressColor: Int,
        progressCornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
    )

    fun updateProgress(progress: Int)
}