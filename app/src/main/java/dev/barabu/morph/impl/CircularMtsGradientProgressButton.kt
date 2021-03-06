package dev.barabu.morph.impl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import dev.barabu.morph.button.MorphingAnimation
import dev.barabu.morph.button.ProgressMorphingButton
import dev.barabu.morph.generator.InterruptibleProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator
import kotlin.math.min

class CircularMtsGradientProgressButton : ProgressMorphingButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private lateinit var sweepGradient: SweepGradient

    private val rectProgress = RectF()
    private var clipPath: Path? = null

    override var generator: ProgressGenerator = InterruptibleProgressGenerator(
        object : ProgressGenerator.OnCompleteListener {
            override fun onComplete() {
                progress = ProgressGenerator.MIN_PROGRESS
                postProgressOp?.invoke()
            }
        }
    )

    /**
     * Метод вызывается при обновлении прогресса в диапазоне (MIN_PROGRESS < p <= MAX_PROGRESS)
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!isMorphingInProgress &&
            progress > ProgressGenerator.MIN_PROGRESS &&
            progress <= ProgressGenerator.MAX_PROGRESS
        ) {
            val circleSize = min(width, height) - ringPadding * 2
            val horMargin = (width - circleSize) / 2f
            val verMargin = (height - circleSize) / 2f
            val clipWidth = circleSize - progressStrokeWidth * 2

            if (clipPath == null) {
                paintProgress.shader = sweepGradient

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

            rectProgress.apply {
                left = 0f
                top = 0f
                bottom = height.toFloat()
                right = width.toFloat()
            }

            canvas?.apply {
                save()

                clipRect(rectProgress)
                clipPathCompat(clipPath!!)

                rotate(
                    360f * (progress.toFloat() / ProgressGenerator.MAX_PROGRESS),
                    width / 2f,
                    height / 2f
                )
                drawArc(
                    horMargin,
                    verMargin,
                    horMargin + circleSize,
                    verMargin + circleSize,
                    0f,
                    360f,
                    true,
                    paintProgress
                )
                restore()
            }
        }
    }

    /**
     * NOTE: Для расчета координат центра нужно использовать размеры ПОСЛЕ морфа. На момент
     * вызова этой функции кнопка еще прямоугольная !!!
     */
    override fun morphToProgress(progressParams: ProgressParams) {

        this.sweepGradient = SweepGradient(
            progressParams.width / 2f,
            progressParams.height / 2f,
            progressParams.colorPrimary,
            progressParams.colorSecondary
        )

        super.morphToProgress(progressParams)
    }

    override fun morphToResult(params: Params) {
        params.apply {
            animationListener = object : MorphingAnimation.Listener {
                override fun onAnimationStart() {
                }

                override fun onAnimationEnd() {
                    unBlockTouch()
                }
            }
        }

        postProgressOp = { morph(params) }
        (generator as InterruptibleProgressGenerator).interrupt()
    }

    override fun updateProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }
}