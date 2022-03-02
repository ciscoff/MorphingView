package dev.barabu.morph.impl

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import dev.barabu.morph.button.AnchorIcon
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
    private lateinit var pathProgress: Path
    private lateinit var pathStamp: Path

    private val dotSize: Float
        get() {
            val circleDiameter = min(width, height) - ringPadding * 2
            return (circleDiameter * PI.toFloat()) / (DOTS_QTY * 2)
        }

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
            val circleSize = min(width, height) - ringPadding * 2

            if (!::pathProgress.isInitialized) {
                pathProgress = Path().apply {
                    addCircle(width / 2f, height / 2f, circleSize / 2f, Path.Direction.CW)
                }

                pathStamp = Path().apply {
                    addOval(RectF(0f, 0f, dotSize, dotSize), Path.Direction.CW)
                }

                paintProgress.apply {
                    pathEffect =
                        PathDashPathEffect(
                            pathStamp,
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

    override fun morphToProgress(
        color: Int,
        progressPrimaryColor: Int,
        progressSecondaryColor: Int,
        progressCornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
        ringPadding: Float
    ) {

        this.sweepGradient =
            SweepGradient(width / 2f, height / 2f, progressPrimaryColor, progressSecondaryColor)

        super.morphToProgress(
            color,
            progressPrimaryColor,
            progressSecondaryColor,
            progressCornerRadius,
            width,
            height,
            duration,
            ringPadding
        )
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
                icon = AnchorIcon(l = iconId),
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
        private const val DOT_SIZE = 10f
        private const val DOTS_QTY = 12
    }
}