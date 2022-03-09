package dev.barabu.morph.impl

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import dev.barabu.morph.button.Gradient
import dev.barabu.morph.button.MorphingAnimation
import dev.barabu.morph.button.ProgressMorphingButton
import dev.barabu.morph.generator.InterruptibleProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator
import kotlin.math.PI
import kotlin.math.min

class CircularDottedProgressButton : ProgressMorphingButton, Gradient {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private lateinit var sweepGradient: SweepGradient

    /**
     * Контур прогресса - окружность
     */
    private lateinit var pathProgress: Path

    /**
     * Отдельная точка в колесе прогресса
     */
    private lateinit var pathDot: Path

    /**
     * Диаметр окружности, на которой будут лежать ЦЕНТРЫ точек.
     */
    private val dotsGuidelineDiameter: Float
        get() = min(width, height) * DOTS_GUIDELINE_RATIO

    /**
     * Расчитать размеры точки в колесе прогресса. Размер точек и промежутков между ними одинаковы.
     * Поэтому длину окружности делим на удвоенное количество точек.
     */
    private val dotSize: Float
        get() = (dotsGuidelineDiameter * PI.toFloat()) / (DOTS_QTY * 2)

    /**
     * Paint для заливки точек прогресса. Она будет красить градиентом.
     */
    override val paintProgress: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
    }

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
            if (!::pathProgress.isInitialized) {
                // контур прогресса - кольцо
                pathProgress = Path().apply {
                    addCircle(width / 2f, height / 2f, dotsGuidelineDiameter / 2f, Path.Direction.CW)
                }

                // отдельная точка
                pathDot = Path().apply {
                    addOval(RectF(0f, 0f, dotSize, dotSize), Path.Direction.CW)
                }

                // эффект рисования Path'a другим Path'ом
                paintProgress.apply {
                    pathEffect =
                        PathDashPathEffect(
                            pathDot,
                            dotSize * 2,
                            0f,
                            PathDashPathEffect.Style.ROTATE
                        )
                    shader = sweepGradient
                }
            }

            canvas?.apply {
                save()
                rotate(
                    360f * (progress.toFloat() / ProgressGenerator.MAX_PROGRESS),
                    width / 2f,
                    height / 2f
                )
                drawPath(pathProgress, paintProgress)
                restore()
            }
        }
    }

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

    companion object {
        /**
         * Количество точек в колесе прогресса
         */
        private const val DOTS_QTY = 12

        /**
         * Отношение ширины окружности, на контуре которой будут лежать ЦЕНТРЫ точек
         * к ширине View.
         */
        private const val DOTS_GUIDELINE_RATIO = 0.8f
    }
}