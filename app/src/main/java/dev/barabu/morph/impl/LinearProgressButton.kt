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
import dev.barabu.morph.generator.IndeterminateProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator.Companion.MAX_PROGRESS
import dev.barabu.morph.generator.ProgressGenerator.Companion.MIN_PROGRESS

class LinearProgressButton : MorphingButton, MorphStateController, ProgressConsumer {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var progressCornerRadius = resources.getDimension(R.dimen.corner_radius_2dp)
    private var progressPrimaryColor: Int = Color.TRANSPARENT
    private var progressSecondaryColor: Int = Color.TRANSPARENT
    private var progress: Int = MIN_PROGRESS

    private val paint: Paint = Paint().apply {
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
            paint.color = progressPrimaryColor

            progressRect.apply {
                bottom = height.toFloat()
                right = (width.toFloat() / MAX_PROGRESS) * progress
            }

            canvas?.drawRoundRect(progressRect, progressCornerRadius, progressCornerRadius, paint)
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

        this.progressCornerRadius = progressCornerRadius
        this.progressPrimaryColor = progressPrimaryColor
        this.progressSecondaryColor = progressSecondaryColor

        val generator =
            IndeterminateProgressGenerator(
                object : ProgressGenerator.OnCompleteListener {
                    override fun onComplete() {
                        progress = MIN_PROGRESS
                        unBlockTouch()
                    }
                })

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
                    generator.start(this@LinearProgressButton)
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
}

