package dev.barabu.runner.circular

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt

//
//    Диаграмма для расчета sweepAngle, который меняется в диапазоне (0..180..0)
//    При этом угол startAngle меняется в диапазоне (0..360)
//
//          Y (sweep angle)
//          |
//          |
//          |
//          |
//     180  | . . . . . . . *
//          |           *   .   *
//          |        *      .      *
//          |     *         .         *
//          |  *            .            *
//         -*---------------.---------------*----------- X (progress)
//         0%              50%             100%
//
//         На участке прогресса (0..50) угол меняется как 'Y = X * 3.6'
//         На участке прогресса (51..100) угол меняется как 'Y = 360 - X * 3.6'
//
//  NOTE: требуется доработка. При таком алгоритме в верхней точке графика "голова" прогресса
//  начинает двигаться навстречу хвосту и только потом замедление головы становится правильным.
//
//  В итоге здесь применен тот же алгоритм, что и в LinearElasticDrawable - и голова и хвост
//  анимируются по отдельности в диапазонах (0..360). При этом в качестве startAngle выступает
//  текущее значение хвоста, а sweepAngle вычисляется как разница между головой и хвостом.

class SimpleCircularElasticDrawable(
    private val parentView: View,
    private val params: Params
) : Drawable(), Animatable {

    private var tailAngle: Float = 0f
    private var sweepAngle: Float = 0f
    private var headAngle: Float = 0f

    private lateinit var compositeAnimator: AnimatorSet

    private val headAngleAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
        interpolator = params.headInterpolator
        repeatCount = ValueAnimator.INFINITE
        duration = params.duration
        addUpdateListener { valueAnimator ->
            headAngle = valueAnimator.animatedValue as Float
        }
    }

    private val tailAngleAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
        interpolator = params.tailInterpolator
        repeatCount = ValueAnimator.INFINITE
        duration = params.duration
        addUpdateListener { valueAnimator ->
            tailAngle = valueAnimator.animatedValue as Float
            sweepAngle = headAngle - tailAngle
        }
    }

    private val progressAnimator = ValueAnimator.ofFloat(MIN_PROGRESS, MAX_PROGRESS).apply {
        interpolator = LinearInterpolator()
        repeatCount = ValueAnimator.INFINITE
        duration = params.duration

        addUpdateListener { valueAnimator ->
            progress = valueAnimator.animatedValue as Float
        }
    }

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

    private val progressRect = RectF()

    override fun draw(canvas: Canvas) {
        canvas.apply {
            save()
            rotate(
                3.60f * progress,
                bounds.width() / 2f,
                bounds.height() / 2f
            )
            drawArc(progressRect, tailAngle, sweepAngle, false, paint)
            restore()
        }
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)

        progressRect.apply {
            left = bounds.left + params.strokeWidth / 2f + .5f
            top = bounds.top + params.strokeWidth / 2f + .5f
            right = bounds.right - params.strokeWidth / 2f - .5f
            bottom = bounds.bottom - params.strokeWidth / 2f - .5f
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

        compositeAnimator = AnimatorSet().apply {
            playTogether(
                headAngleAnimator,
                tailAngleAnimator
            )
        }
        compositeAnimator.start()
        progressAnimator.start()
    }

    override fun stop() {
        if (!isRunning) {
            return
        }
        progressAnimator.end()
        compositeAnimator.end()
    }

    override fun isRunning(): Boolean = progressAnimator.isRunning

    // Это функции графика (см выше)
    private fun ascendingSweepAngle(progress: Float) = progress * 3.6f
    private fun descendingSweepAngle(progress: Float) = 360f - progress * 3.6f

    data class Params(
        val headInterpolator: DecelerateInterpolator,
        val tailInterpolator: AccelerateInterpolator,
        val strokeWidth: Float,
        val duration: Long,
        @ColorInt val color: Int
    )

    companion object {
        const val MIN_PROGRESS = 0F
        const val MIDDLE_PROGRESS = 50f
        const val MAX_PROGRESS = 100F
    }
}