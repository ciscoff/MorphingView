package dev.barabu.morph.button

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import dev.barabu.morph.R
import dev.barabu.morph.generator.ProgressGenerator

abstract class ProgressMorphingButton : MorphingButton, ProgressConsumer {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    // Основной цвет прогресса
    protected var primaryColor: Int = Color.TRANSPARENT

    // Дополнительный цвет прогресса (например фон)
    protected var secondaryColor: Int = Color.TRANSPARENT

    // Текущий прогресс в диапазоне от 0 до 100
    protected var progress: Int = ProgressGenerator.MIN_PROGRESS

    // Толщина кольца прогресса
    protected val progressStrokeWidth =
        resources.getDimension(R.dimen.cycle_progress_stroke_width_4dp)

    // Это отступ кольца прогресса от края View, чтобы кольцо было внутри элемента, а не по кромке
    protected var ringPadding: Float = 0f

    protected open val paintProgress: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    // Callback по окончании прогресса
    protected var postProgressOp: (() -> Unit)? = null

    // Генератор прогресса
    protected abstract var generator: ProgressGenerator

    override fun updateProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    /**
     * Любой morph - это смена пары цветов colorNormal/colorPressed
     */
    open fun morphToProgress(
        color: Int,
        progressPrimaryColor: Int,
        progressSecondaryColor: Int,
        progressCornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
        ringPadding: Float = 0f,
        strokeColor: Int,
        strokeWidth: Int
    ) {
        // todo вот этот блок не нужен. он повторяется в morph()
        this.cornerRadius = progressCornerRadius
        this.primaryColor = progressPrimaryColor
        this.secondaryColor = progressSecondaryColor
        this.ringPadding = ringPadding

        blockTouch()

        val params = Params(
            cornerRadius = progressCornerRadius,
            width = width,
            height = height,
            colorNormal = color,
            colorPressed = color,
            duration = duration,
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
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

    /**
     * Любой morph - это смена пары цветов colorNormal/colorPressed
     */
    abstract fun morphToResult(
        colorNormal: Int,
        colorPressed: Int,
        cornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
        iconId: Int,
        strokeColor: Int,
        strokeWidth: Int
    )

    /**
     * Region.Op.DIFFERENCE - для рисования выбирается область первого прямоугольника
     * за исключением пересечения со вторым элементом. То есть а данном случае из отрисовки
     * исключаем область заданную canvas.clipPath (у нас это круг).
     *
     * Визуально работу различных Region.Op можно посмотреть тут:
     * https://startandroid.ru/ru/uroki/vse-uroki-spiskom/325-urok-147-risovanie-region.html
     */
    protected fun clipPathCompat(canvas: Canvas, path: Path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(path)
        } else {
            canvas.clipPath(path, Region.Op.DIFFERENCE)
        }
    }
}