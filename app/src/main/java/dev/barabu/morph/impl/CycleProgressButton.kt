package dev.barabu.morph.impl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import dev.barabu.morph.R
import dev.barabu.morph.button.MorphingAnimation
import dev.barabu.morph.button.MorphingButton
import dev.barabu.morph.button.ProgressButton
import dev.barabu.morph.impl.ProgressGenerator.Companion.MAX_PROGRESS
import dev.barabu.morph.impl.ProgressGenerator.Companion.MIN_PROGRESS

class CycleProgressButton : MorphingButton, ProgressButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var progressCornerRadius = resources.getDimension(R.dimen.corner_radius_2dp)
    private var progressColor: Int = Color.TRANSPARENT
    private var progress: Int = MIN_PROGRESS
    private var color: Int = Color.TRANSPARENT

    private val paintProgress: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val paintCycle: Paint = Paint().apply {
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
            paintProgress.color = progressColor
            paintCycle.color = color

            progressRect.apply {
                left = 0f
                top = 0f
                bottom = height.toFloat()
                right = width.toFloat()
            }

            canvas?.drawArc(
                progressRect,
                0f,
                360f / MAX_PROGRESS * progress,
                true,
                paintProgress
            )

            val strokeWidth = 10f
            progressRect.apply {
                left = strokeWidth
                top = strokeWidth
                bottom = height.toFloat() - strokeWidth
                right = width.toFloat() - strokeWidth
            }

            canvas?.drawOval(progressRect, paintCycle)
        }
    }

    override fun morphToProgress(
        color: Int,
        progressColor: Int,
        progressCornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
    ) {

        this.color = color
        this.progressColor = progressColor
        this.progressCornerRadius = progressCornerRadius

        val generator = ProgressGenerator(object : ProgressGenerator.OnCompleteListener {
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
                    generator.start(this@CycleProgressButton)
                }
            }
        )
        morph(params)
    }

    override fun updateProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }
}

