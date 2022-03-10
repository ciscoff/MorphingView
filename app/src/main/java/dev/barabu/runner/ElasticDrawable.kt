package dev.barabu.runner

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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

/**
 * --------------------------------------------------------------------------------------------
 * Немного теории по исходникам классов View и Drawable. Итак Drawable может использоваться
 * как фон для View. При это сам Drawable ничего не знает о том в какой View он "работает".
 * Однако существует такой интерфейс как Drawable.Callback, через который Drawable оповещает
 * внешний мир об изменениях своих свойств, например поменяли цвет заливки или цвет обводки и
 * drawable оповещает об этом внешний мир вызовом:
 *   callback.invalidateDrawable(this)
 *
 * Внешний мир (а это View), используя, например, Drawable в качестве background, указывает
 * себя как имплементатора интерфейса Drawable.Callback. Вот как выглядит декларация View:
 *
 *   class View implements Drawable.Callback ....
 *
 * Теперь как только мы поменяем цвет в Drawable, то он вызывет Callback, View получит
 * управление и в итоге вызовет свою invalidate. Все это приводит к последующему View::onDraw,
 * и уже там мы вызываем drawable.draw(canvas). Вот так это работает.
 * --------------------------------------------------------------------------------------------
 *
 * Алгоритм такой - при создании инстанса этого класса сразу запускаются два ValueAnimator для
 * X-координат линии прогресса. [headAnimator] анимирует X-координату головы линии, [tailAnimator]
 * анимирует X-координату хвоста линии. Они работют сами по себе и просто меняют значения полей
 * [head]/[tail]. Вместе с ними запускается третий аниматор [progressAnimator], который анимирует
 * величину прогресса в диапазоне (MIN_PROGRESS..MAX_PROGRESS). При каждом увеличении прогресса
 * он оповещает родительскую View вызовом parentView.invalidate(). Та в свою очередь попадает
 * в onDraw где и вызывает ElasticDrawable::draw. Последний метод читает текущие [head]/[tail] и
 * рисует между ними линию. Вот и все.
 */
class ElasticDrawable(
    private val parentView: View,
    private val params: Params
) : Drawable(), Animatable {

    private var tail: Int = 0
    private var head: Int = 0

    private lateinit var compositeAnimator : AnimatorSet

    // Я так полагаю, что оба эти аниматора просыпаются при каждом тике и их методы updateListener
    // вызываются по очереди в порядке их добавления в AnimatorSet. Поэтому если tailAnimation
    // добавить после headAnimation, то он будет обновлять tail уже после того как обновлена head,
    // а значит оба значения актуальны и имея актуальные значения мы может сообщить их куда-то (?)
    private val headAnimator = ValueAnimator.ofInt(params.moveFrom, params.moveTo).apply {
        interpolator = params.headInterpolator
        repeatCount = ValueAnimator.INFINITE
        duration = params.duration
        addUpdateListener { valueAnimator ->
            head = valueAnimator.animatedValue as Int
        }
    }

    private val tailAnimator = ValueAnimator.ofInt(params.moveFrom, params.moveTo).apply {
        interpolator = params.tailInterpolator
        repeatCount = ValueAnimator.INFINITE
        duration = params.duration
        addUpdateListener { valueAnimator ->
            tail = valueAnimator.animatedValue as Int
        }
    }

    private val progressAnimator = ValueAnimator.ofFloat(MIN_PROGRESS, MAX_PROGRESS).apply {
        interpolator = LinearInterpolator()
        repeatCount = ValueAnimator.INFINITE
        duration = params.duration

        addUpdateListener { valueAnimator ->
            progress = valueAnimator.animatedValue as Float
        }

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator?) {
                if(tailAnimator.isRunning || headAnimator.isRunning) {
                    compositeAnimator.cancel()
                    headAnimator.setIntValues(params.moveFrom, params.moveTo)
                    tailAnimator.setIntValues(params.moveFrom, params.moveTo)
                    compositeAnimator.start()
                }
            }
        })
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = params.color
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

        progressRect.apply {
            left = tail.toFloat()
            top = 0f
            right = head.toFloat()
            bottom = bounds.bottom.toFloat()
        }
        canvas.drawRoundRect(progressRect, bounds.height() / 2f, bounds.height() / 2f, paint)
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
                headAnimator,
                tailAnimator
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

    data class Params(
        val headInterpolator: DecelerateInterpolator,
        val tailInterpolator: AccelerateInterpolator,
        val moveFrom: Int,
        val moveTo: Int,
        val duration: Long,
        @ColorInt val color: Int
    )

    companion object {
        const val MIN_PROGRESS = 0F
        const val MAX_PROGRESS = 100F
    }
}