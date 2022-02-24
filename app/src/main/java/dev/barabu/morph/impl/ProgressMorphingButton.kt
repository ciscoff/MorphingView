package dev.barabu.morph.impl

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import dev.barabu.morph.R
import dev.barabu.morph.button.MorphingAnimation
import dev.barabu.morph.button.MorphingButton
import dev.barabu.morph.button.ProgressConsumer
import dev.barabu.morph.generator.InterruptibleProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator
import kotlin.math.min

class ProgressMorphingButton : MorphingButton, ProgressConsumer {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val progressStrokeWidth = resources.getDimension(R.dimen.cycle_progress_stroke_width)
    private var progressCornerRadius = resources.getDimension(R.dimen.corner_radius_2dp)
    private var gradientStartColor: Int = Color.TRANSPARENT
    private var gradientEndColor: Int = Color.TRANSPARENT
    private var progress: Int = ProgressGenerator.MIN_PROGRESS

    private lateinit var sweepGradient: SweepGradient

    private val rectProgress = RectF()
    private var clipPath: Path? = null

    private val paintProgress: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private var postProgressOp: (() -> Unit)? = null

    private val generator = InterruptibleProgressGenerator(
        object : InterruptibleProgressGenerator.OnCompleteListener {
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

        if (!isMorphingInProgress && progress > ProgressGenerator.MIN_PROGRESS && progress <= ProgressGenerator.MAX_PROGRESS) {

            val circleSize = min(width, height)
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
                drawColor(Color.TRANSPARENT)
                clipRect(rectProgress)
                clipPathCompat(canvas, clipPath!!)
                rotate(360f * (progress.toFloat() / ProgressGenerator.MAX_PROGRESS), width / 2f, height / 2f)
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

    fun morphToProgress(
        color: Int,
        progressPrimaryColor: Int,
        progressSecondaryColor: Int,
        progressCornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
    ) {
        this.gradientEndColor = progressPrimaryColor
        this.gradientStartColor = progressSecondaryColor
        this.progressCornerRadius = progressCornerRadius

        this.sweepGradient =
            SweepGradient(width / 2f, height / 2f, progressPrimaryColor, progressSecondaryColor)

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
                    generator.start(this@ProgressMorphingButton)
                }
            }
        )
        morph(params)
    }

    fun morphToState(
        colorNormal: Int,
        colorPressed: Int,
        cornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
        iconId: Int
    ) {
        generator.interrupt()

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

    /**
     * Region.Op.DIFFERENCE - для рисования выбирается область первого прямоугольника
     * за исключением пересечения со вторым элементом. То есть а данном случае из отрисовки
     * исключаем область заданную canvas.clipPath (у нас это круг).
     *
     * Визуально работу различных Region.Op можно посмотреть тут:
     * https://startandroid.ru/ru/uroki/vse-uroki-spiskom/325-urok-147-risovanie-region.html
     */
    private fun clipPathCompat(canvas: Canvas, path: Path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(path)
        } else {
            canvas.clipPath(path, Region.Op.DIFFERENCE)
        }
    }
}