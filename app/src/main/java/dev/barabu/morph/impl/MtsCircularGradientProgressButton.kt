package dev.barabu.morph.impl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import dev.barabu.morph.button.AnchorIcon
import dev.barabu.morph.button.MorphingAnimation
import dev.barabu.morph.button.ProgressMorphingButton
import dev.barabu.morph.generator.InterruptibleProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator
import kotlin.math.min

class MtsCircularGradientProgressButton : ProgressMorphingButton {

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
                clipPathCompat(canvas, clipPath!!)

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

    override fun morphToResult(
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
}