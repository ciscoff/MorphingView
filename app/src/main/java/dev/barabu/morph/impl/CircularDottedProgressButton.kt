package dev.barabu.morph.impl

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import dev.barabu.morph.R
import dev.barabu.morph.button.AnchorIcon
import dev.barabu.morph.button.MorphingAnimation
import dev.barabu.morph.button.ProgressMorphingButton
import dev.barabu.morph.generator.InterruptibleProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator
import kotlin.math.min

class CircularDottedProgressButton : ProgressMorphingButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val rectProgress = RectF()
    private var clipPath: Path? = null

    override val paintProgress: Paint = Paint().apply {
//        val dotSize = resources.getDimension(R.dimen.cycle_progress_dot)

        val dotSize = 10f

        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = dotSize
        pathEffect = DashPathEffect(floatArrayOf(dotSize, dotSize), 0f)
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
            val horMargin = (width - circleSize) / 2f
            val verMargin = (height - circleSize) / 2f
            val clipWidth = circleSize - progressStrokeWidth * 2

//            if (clipPath == null) {
//
//                val clipX = (width - clipWidth) / 2
//                val clipY = (height - clipWidth) / 2
//
//                clipPath = Path().apply {
//                    addArc(
//                        clipX,
//                        clipY,
//                        clipX + clipWidth,
//                        clipY + clipWidth,
//                        0f,
//                        360f
//                    )
//                }
//            }

//            rectProgress.apply {
//                left = 0f
//                top = 0f
//                bottom = height.toFloat()
//                right = width.toFloat()
//            }

            canvas?.apply {
                save()

//                clipRect(rectProgress)
//                clipPathCompat(canvas, clipPath!!)

                rotate(
                    360f * (progress.toFloat() / ProgressGenerator.MAX_PROGRESS),
                    width / 2f,
                    height / 2f
                )

                paintProgress.color = primaryColor
                drawCircle(width / 2f, height / 2f, circleSize / 2f, paintProgress)

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