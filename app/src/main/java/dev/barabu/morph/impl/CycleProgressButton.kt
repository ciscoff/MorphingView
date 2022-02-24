package dev.barabu.morph.impl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import dev.barabu.morph.R
import dev.barabu.morph.button.MorphingAnimation
import dev.barabu.morph.button.MorphingButton
import dev.barabu.morph.button.MorphStateController
import dev.barabu.morph.button.ProgressConsumer
import dev.barabu.morph.generator.LinearProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator.Companion.MAX_PROGRESS
import dev.barabu.morph.generator.ProgressGenerator.Companion.MIN_PROGRESS

class CycleProgressButton : MorphingButton, MorphStateController, ProgressConsumer {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var color: Int = Color.TRANSPARENT
    private var progress: Int = MIN_PROGRESS
    private var progressPrimaryColor: Int = Color.TRANSPARENT
    private var progressSecondaryColor: Int = Color.TRANSPARENT
    private val progressStrokeWidth = resources.getDimension(R.dimen.cycle_progress_stroke_width)
    private var progressCornerRadius = resources.getDimension(R.dimen.corner_radius_2dp)

    private val paintProgress: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val paintClipCycle: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val progressRect = RectF()

    /**
     * Этот метод работает только для рисования "линии прогресса"
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!isMorphingInProgress && progress > MIN_PROGRESS && progress <= MAX_PROGRESS) {

            // Область для рисования индикатора прогресса
            progressRect.apply {
                left = 0f
                top = 0f
                bottom = height.toFloat()
                right = width.toFloat()
            }

            // Фон для прогресса (круг)
            paintProgress.color = progressSecondaryColor
            canvas?.drawOval(progressRect, paintProgress)

            // Индикатор прогресса поверх фона (Arc)
            paintProgress.color = progressPrimaryColor
            canvas?.drawArc(
                progressRect,
                360f * (progress.toFloat() / MAX_PROGRESS),
                SWEEP_ANGLE,
                true,
                paintProgress
            )

            // Clipping в центре в форме круга
            progressRect.apply {
                left = progressStrokeWidth
                top = progressStrokeWidth
                bottom = height.toFloat() - progressStrokeWidth
                right = width.toFloat() - progressStrokeWidth
            }
            paintClipCycle.color = color
            canvas?.drawOval(progressRect, paintClipCycle)
        }
    }

    override fun morphToProgress(
        color: Int,
        progressPrimaryColor: Int,
        progressSecondaryColor: Int,
        progressCornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
    ) {

        this.color = color
        this.progressPrimaryColor = progressPrimaryColor
        this.progressCornerRadius = progressCornerRadius
        this.progressSecondaryColor = progressSecondaryColor

        val generator = LinearProgressGenerator(object : ProgressGenerator.OnCompleteListener {
            override fun onComplete() {
                progress = MIN_PROGRESS
                unBlockTouch()
            }
        }, false)

        blockTouch()

        val params = Params(
            cornerRadius = progressCornerRadius,
            width = width,
            height = height,
            colorNormal = color,
            colorPressed = color,
            duration = duration,
            animationListener = object : MorphingAnimation.Listener {
                override fun onAnimationStart() {
                }

                // Сразу после морфа формы кнопки запускаем анимацию прогресса
                override fun onAnimationEnd() {
                    generator.start(this@CycleProgressButton)
                }
            }
        )
        morph(params)
    }

    override fun morphToState(
        state: MorphStateController.State,
        colorNormal: Int,
        colorPressed: Int,
        cornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
        @DrawableRes iconId: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun updateProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    companion object {
        private const val SWEEP_ANGLE = 80f
    }
}

