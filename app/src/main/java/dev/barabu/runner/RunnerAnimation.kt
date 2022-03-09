package dev.barabu.runner

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.annotation.ColorInt
import com.google.android.material.animation.AnimatorSetCompat.playTogether

/**
 * Алгоритм такой - при создании инстанса этого класса сразу запускаются два ValueAnimator.
 * [headAnimation] анимирует X-координату головы линии, [tailAnimation] анимирует X-координату
 * хвоста линии. Они работют сами по себе и просто меняют значения полей head/tail соответственно.
 *
 * Существует какая-то внешняя View, которая создала данный Drawable и рисует его как свой фон.
 * В этой View есть генератор, который линейно генерит прогресс (от 0 до 100) и это заставляет
 * View обновлять наше поле [progress]
 */
class RunnerAnimation(
    private val ownerView: View,
    private val params: Params
) : Drawable(), Animatable {

    private var tail: Int = 0
    private var head: Int = 0

    private val tailAnimation = ValueAnimator.ofInt(params.moveFrom, params.moveTo).apply {
        interpolator = params.tailInterpolator
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener { valueAnimator ->
            tail = valueAnimator.animatedValue as Int
        }
    }

    private val headAnimation = ValueAnimator.ofInt(params.moveFrom, params.moveTo).apply {
        interpolator = params.headInterpolator
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener { valueAnimator ->
            head = valueAnimator.animatedValue as Int
        }

        addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
                // todo
            }
        })
    }

    private val indeterminateAnimator = AnimatorSet().apply {
        playTogether(
            tailAnimation,
            headAnimation
        )
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = params.strokeWidth
        color = params.color
    }

    var progress: Float = 0f
        set(value) {
            if (value == field) {
                return
            }

            field = when {
                value > MAX_PROGRESS -> MAX_PROGRESS
                value < MIN_PROGRESS -> MIN_PROGRESS
                else -> value
            }

            ownerView.invalidate()
        }

    private val progressRect = Rect()


    override fun draw(canvas: Canvas) {
        progressRect.apply {
            left = tail
            top = 0
            right = head
            bottom = bounds.bottom
        }
        canvas.drawRect(progressRect, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.TRANSPARENT

    override fun start() {
        if (isRunning) {
            return
        }
        indeterminateAnimator.start()
    }

    override fun stop() {
        if (!isRunning) {
            return
        }
        indeterminateAnimator.end()
    }

    override fun isRunning(): Boolean = indeterminateAnimator.isRunning

    data class Params(
        val tailInterpolator: AccelerateDecelerateInterpolator,
        val headInterpolator: AccelerateInterpolator,
        val moveFrom: Int,
        val moveTo: Int,
        @ColorInt val color: Int,
        val strokeWidth: Float
    )

    companion object {
        const val MIN_PROGRESS = 0F
        const val MAX_PROGRESS = 100F
    }
}