package dev.barabu.runner.circular

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import kotlin.math.floor

/**
 * Два режима:
 * - голова убегаетц
 * - хвост догоняет
 * режимы работают по очереди
 *
 */
class AdvancedCircularElasticDrawable(
    private val parentView: View,
    private val params: Params
) : Drawable(), Animatable {

    enum class AnimationMode {
        RunAway,
        Chase
    }

    private var animationMode: AnimationMode = AnimationMode.RunAway

    private var startAngle: Float = 0f
    private var sweepAngle: Float = 0f
    private var totalAngle: Float = 0f

    private val progressRect = RectF()

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = params.color
        strokeWidth = params.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    private var progress: Float = MIN_PROGRESS
        set(value) {
            if (value == field) {
                return
            }

            field = when {
                value > MAX_PROGRESS -> MAX_PROGRESS
                value < MIN_PROGRESS -> MIN_PROGRESS
                else -> value
            }
            parentView.invalidate()
        }

    private val sweepAngleAnimator = ValueAnimator.ofFloat(0f, 360f - GAP_ANGLE).apply {
        interpolator = params.headInterpolator
        repeatCount = ValueAnimator.INFINITE
        duration = params.duration
        addUpdateListener { valueAnimator ->
            sweepAngle = floor(valueAnimator.animatedValue as Float + 0.5f)

            when (animationMode) {
                // Голова убегает от хвоста. Хвост (startAngle) не меняется.
                AnimationMode.RunAway -> {
                    totalAngle = (startAngle + sweepAngle) % 360
                }
                // Хвост догоняет голову. Голова (totalAngle) не меняется. Изменяется хвост
                // (startAngle) благодаря уменьшению sweepAngle.
                AnimationMode.Chase -> {
                    startAngle = totalAngle - sweepAngle
                }
            }
        }

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator?) {
                swapAnimationMode()
            }
        })
    }

    private val progressAnimator = ValueAnimator.ofFloat(MIN_PROGRESS, MAX_PROGRESS).apply {
        interpolator = LinearInterpolator()
        repeatCount = ValueAnimator.INFINITE
        duration = params.duration

        addUpdateListener { valueAnimator ->
            progress = valueAnimator.animatedValue as Float
        }
    }

    private fun swapAnimationMode() {
        when (animationMode) {
            AnimationMode.Chase -> {
                // Голова будет убегать от хвоста
                animationMode = AnimationMode.RunAway
                sweepAngleAnimator.setFloatValues(GAP_ANGLE, 360f - 2 * GAP_ANGLE)
            }
            AnimationMode.RunAway -> {
                // Хвост будет догонять голову
                animationMode = AnimationMode.Chase
                sweepAngleAnimator.setFloatValues(360f - 2 * GAP_ANGLE, GAP_ANGLE)
            }
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.apply {
            save()
            rotate(
                3.60f * progress,
                bounds.width() / 2f,
                bounds.height() / 2f
            )
            drawArc(progressRect, startAngle, sweepAngle, false, paint)
            restore()
        }
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        progressRect.apply {
            left = bounds.left + params.strokeWidth * 1.5f + .5f
            top = bounds.top + params.strokeWidth * 1.5f + .5f
            right = bounds.right - params.strokeWidth * 1.5f - .5f
            bottom = bounds.bottom - params.strokeWidth * 1.5f - .5f
        }
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
        sweepAngleAnimator.start()
        progressAnimator.start()
    }

    override fun stop() {
        if (!isRunning) {
            return
        }
        progressAnimator.end()
        sweepAngleAnimator.end()
    }

    override fun isRunning(): Boolean = progressAnimator.isRunning

    data class Params(
        val headInterpolator: DecelerateInterpolator,
        val strokeWidth: Float,
        val duration: Long,
        @ColorInt val color: Int
    )

    companion object {
        const val MIN_PROGRESS = 0F
        const val MAX_PROGRESS = 100F
        const val GAP_ANGLE = 45f
    }
}