package dev.barabu.morph.impl

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import dev.barabu.morph.button.MorphingAnimation
import dev.barabu.morph.button.ProgressMorphingButton
import dev.barabu.morph.generator.InterruptibleProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator.Companion.MAX_PROGRESS
import dev.barabu.morph.generator.ProgressGenerator.Companion.MIN_PROGRESS
import kotlin.math.min

class CircularColoredProgressButton : ProgressMorphingButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val rectProgress = RectF()
    private var clipPath: Path? = null

    override var generator: ProgressGenerator = InterruptibleProgressGenerator(
        object : ProgressGenerator.OnCompleteListener {
            override fun onComplete() {
                progress = MIN_PROGRESS
                postProgressOp?.invoke()
            }
        }
    )

    /**
     * Этот метод работает только для рисования "линии прогресса"
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!isMorphingInProgress
            && progress > MIN_PROGRESS
            && progress <= MAX_PROGRESS
        ) {

            val circleSize = min(width, height)
            val horMargin = (width - circleSize) / 2f
            val verMargin = (height - circleSize) / 2f
            val clipWidth = circleSize - progressStrokeWidth * 2

            if (clipPath == null) {

                val clipX = (width - clipWidth) / 2
                val clipY = (height - clipWidth) / 2

                clipPath = Path().apply {
                    addArc(
                        clipX,
                        clipY,
                        clipX + clipWidth,
                        clipY + clipWidth,
                        0f,
                        360f
                    )
                }
            }

            // Область для рисования индикатора прогресса
            rectProgress.apply {
                left = 0f
                top = 0f
                bottom = height.toFloat()
                right = width.toFloat()
            }

            canvas?.apply {

                save()
                drawColor(Color.TRANSPARENT)

                // Clipping в центре в форме круга
                clipRect(rectProgress)
                clipPathCompat(canvas, clipPath!!)

                rectProgress.apply {
                    left = horMargin
                    top = verMargin
                    bottom = horMargin + circleSize
                    right = verMargin + circleSize
                }

                // Фон для прогресса (круг)
                paintProgress.color = secondaryColor
                drawOval(rectProgress, paintProgress)

                // Индикатор прогресса поверх фона (Arc)
                paintProgress.color = primaryColor
                drawArc(
                    rectProgress,
                    360f * (progress.toFloat() / MAX_PROGRESS),
                    SWEEP_ANGLE,
                    true,
                    paintProgress
                )
                restore()
            }
        }
    }

    override fun morphToFinish(
        colorNormal: Int,
        colorPressed: Int,
        cornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
        iconId: Int
    ) {
        (generator as InterruptibleProgressGenerator).interrupt()

        postProgressOp = {
            val params = Params(
                cornerRadius = cornerRadius,
                width = width,
                height = height,
                colorNormal = colorNormal,
                colorPressed = colorPressed,
                duration = duration,
                icon = iconId,
                animationListener = object : MorphingAnimation.Listener {
                    override fun onAnimationStart() {
                    }

                    override fun onAnimationEnd() {
                        unBlockTouch()
                    }
                }
            )
            morph(params)
        }
    }

    override fun updateProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    companion object {
        private const val SWEEP_ANGLE = 60f
    }
}

