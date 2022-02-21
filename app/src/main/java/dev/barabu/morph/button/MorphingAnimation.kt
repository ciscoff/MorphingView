package dev.barabu.morph.button

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import androidx.core.animation.addListener

/**
 * Анимация отдельной кнопки MorphingButton, получаемой через конструктор.
 */
class MorphingAnimation(private val params: Params) {

    interface Listener {
        fun onAnimationStart()
        fun onAnimationEnd()
    }

    data class Params(
        val button: MorphingButton,

        val cornerRadiusFrom: Float,
        val cornerRadiusTo: Float,

        val widthFrom: Int,
        val widthTo: Int,

        val heightFrom: Int,
        val heightTo: Int,

        val strokeWidthFrom: Int,
        val strokeWidthTo: Int,

        val strokeColorFrom: Int,
        val strokeColorTo: Int,

        val colorFrom: Int,
        val colorTo: Int,

        val duration: Int,

        val animationListener: Listener? = null
    )

    fun start() {

        val background = params.button.drawableNormal

        val cornerAnimation = ObjectAnimator.ofFloat(
            background,
            PROP_CORNER_RADIUS,
            params.cornerRadiusFrom,
            params.cornerRadiusTo
        )

        val strokeWidthAnimation = ObjectAnimator.ofInt(
            background,
            PROP_STROKE_WIDTH,
            params.strokeWidthFrom,
            params.strokeWidthTo
        )

        val strokeColorAnimation = ObjectAnimator.ofInt(
            background,
            PROP_STROKE_COLOR,
            params.strokeColorFrom,
            params.strokeColorTo
        ).apply { setEvaluator(ArgbEvaluator()) }

        val backgroundColorAnimation = ObjectAnimator.ofInt(
            background,
            PROP_BACKGROUND_COLOR,
            params.colorFrom,
            params.colorTo
        ).apply { setEvaluator(ArgbEvaluator()) }

        val heightAnimation = ValueAnimator.ofInt(params.heightFrom, params.heightTo).apply {
            addUpdateListener { valueAnimator ->
                params.button.layoutParams = params.button.layoutParams.apply {
                    height = valueAnimator.animatedValue as Int
                }
            }
        }
        val widthAnimation = ValueAnimator.ofInt(params.widthFrom, params.widthTo).apply {
            addUpdateListener { valueAnimator ->
                params.button.layoutParams = params.button.layoutParams.apply {
                    width = valueAnimator.animatedValue as Int
                }
            }
        }

        val animatorSet = AnimatorSet().apply {
            duration = params.duration.toLong()
            playTogether(
                cornerAnimation,
                strokeWidthAnimation,
                strokeColorAnimation,
                backgroundColorAnimation,
                heightAnimation,
                widthAnimation
            )
            addListener(onEnd = { params.animationListener?.onAnimationEnd() })
        }
        animatorSet.start()
        params.animationListener?.onAnimationStart()
    }

    companion object {
        private const val PROP_CORNER_RADIUS = "cornerRadius"
        private const val PROP_STROKE_WIDTH = "strokeWidth"
        private const val PROP_STROKE_COLOR = "strokeColor"
        private const val PROP_BACKGROUND_COLOR = "color"
    }
}