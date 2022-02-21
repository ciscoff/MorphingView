package dev.barabu.morph.button

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.StateSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import dev.barabu.morph.GradientDrawableDelegate
import dev.barabu.morph.R

/**
 * БЛЯ !!!!
 *
 * Тут обязательно нужно использовать ЯВНЫЕ конструкторы, а не '@JvmOverloads constructor' с блоком
 * init {}.
 *
 * Дело в том, что перед тем как вызвать initView() обязательно должен отработать конструктор
 * суперкласса. Именно он прочитает все атрибуты из XML и инициализирует внутренние переменные
 * инстанса кнопки.
 *
 * Если мы используем сахар '@JvmOverloads constructor', то все это ложится на нас. Поэтому при
 * комбинации сахара и блока init {} (в котором тот же код что и в initView()) мы получаем
 * неинициализированную кнопку, а именнно - текст прижат в левый верхний угол, буквы не заглавные,
 * паддинги не определены и равны 0.
 *
 * NOTE: Запомни, что блок init {} - это часть primary конструктора и он отработает раньше
 * конструктора кнопки !!!
 */
open class MorphingButton : AppCompatButton {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    lateinit var drawableNormal: GradientDrawableDelegate
    private lateinit var drawablePressed: GradientDrawableDelegate
    private lateinit var padding: Padding

    private var cornerRadius = resources.getDimension(R.dimen.corner_radius_2dp)
    private var colorNormal = ContextCompat.getColor(context, android.R.color.holo_blue_light)
    private var colorPressed = ContextCompat.getColor(context, android.R.color.holo_blue_dark)
    private var strokeColor = Color.TRANSPARENT
    private var strokeWidth = STROKE_WIDTH_ZERO
    protected var isMorphingInProgress: Boolean = false

    private fun initView() {
        padding = Padding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        drawableNormal = createDrawable(colorNormal, cornerRadius, STROKE_WIDTH_ZERO)
        drawablePressed = createDrawable(colorPressed, cornerRadius, STROKE_WIDTH_ZERO)

        val background = StateListDrawable().apply {
            val statePressed = intArrayOf(android.R.attr.state_pressed)
            addState(statePressed, drawablePressed.gradientDrawable)
            addState(StateSet.WILD_CARD, drawableNormal.gradientDrawable)
        }
        setBackground(background)
    }

    open fun morph(params: Params) {
        if (isMorphingInProgress) {
            return
        }

        drawablePressed.apply {
            color = params.colorPressed
            cornerRadius = params.cornerRadius
            strokeColor = params.strokeColor
            strokeWidth = params.strokeWidth
        }

        if (params.duration == 0) {
            morphWithoutAnimation(params)
        } else {
            morphWithAnimation(params)
        }

        colorNormal = params.colorNormal
        strokeColor = params.strokeColor
        strokeWidth = params.strokeWidth
        cornerRadius = params.cornerRadius
    }

    @SuppressLint("ClickableViewAccessibility")
    fun blockTouch() {
        setOnTouchListener { _, _ -> true }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun unBlockTouch() {
        setOnTouchListener { _, _ -> false }
    }

    private fun morphWithoutAnimation(params: Params) {
        drawableNormal.apply {
            color = params.colorNormal
            cornerRadius = params.cornerRadius
            strokeColor = params.strokeColor
            strokeWidth = params.strokeWidth
        }

        if (params.width != 0 && params.height != 0) {
            layoutParams = layoutParams.apply {
                width = params.width
                height = params.height
            }
        }
        finalizeMorphing(params)
    }

    private fun morphWithAnimation(params: Params) {
        isMorphingInProgress = true

        // Убрать текст и иконку
        text = null
        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        setPadding(padding.left, padding.top, padding.right, padding.bottom)

        val animationParams = MorphingAnimation.Params(
            button = this,
            cornerRadiusFrom = cornerRadius,
            cornerRadiusTo = params.cornerRadius,
            widthFrom = width,
            widthTo = params.width,
            heightFrom = height,
            heightTo = params.height,
            strokeWidthFrom = strokeWidth,
            strokeWidthTo = params.strokeWidth,
            strokeColorFrom = strokeColor,
            strokeColorTo = params.strokeColor,
            colorFrom = colorNormal,
            colorTo = params.colorNormal,
            duration = params.duration,
            animationListener = object : MorphingAnimation.Listener {
                override fun onAnimationStart() {
                    params.animationListener?.onAnimationStart()
                    isClickable = false
                }

                override fun onAnimationEnd() {
                    isClickable = true
                    params.animationListener?.onAnimationEnd()
                    finalizeMorphing(params)
                }
            }
        )
        MorphingAnimation(animationParams).start()
    }

    /**
     * После изменения формы и цвета кнопки добавить текст и иконку
     */
    private fun finalizeMorphing(params: Params) {
        isMorphingInProgress = false

        when {
            params.icon != 0 && params.text != null -> {
                setIconLeft(params.icon)
                text = params.text
            }
            params.icon != 0 -> {
                setIcon(params.icon)
            }
            params.text != null -> {
                text = params.text
            }
        }
    }

    private fun setIconLeft(@DrawableRes drawableId: Int) {
        setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0)
    }

    private fun setIcon(@DrawableRes drawableId: Int) {
        ContextCompat.getDrawable(context, drawableId)?.let { drawable ->
            post {
                val padding = width / 2 - drawable.intrinsicWidth / 2
                setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0)
                setPadding(padding, 0, 0, 0)
            }
        }
    }

    private fun createDrawable(
        color: Int,
        cornerRadius: Float,
        strokeWidth: Int
    ): GradientDrawableDelegate {
        return GradientDrawableDelegate(GradientDrawable()).apply {
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            this.color = color
            this.strokeColor = color
            this.cornerRadius = cornerRadius
            this.strokeWidth = strokeWidth
        }
    }

    data class Padding(val left: Int, val top: Int, val right: Int, val bottom: Int)

    data class Params(
        val cornerRadius: Float,
        val width: Int,
        val height: Int,
        val colorNormal: Int,
        val colorPressed: Int,
        val strokeColor: Int = Color.TRANSPARENT,
        val strokeWidth: Int = STROKE_WIDTH_ZERO,
        val duration: Int = 0,
        val text: String? = null,
        @DrawableRes val icon: Int = 0,
        val animationListener: MorphingAnimation.Listener? = null
    )

    companion object {
        private const val STROKE_WIDTH_ZERO = 0
    }
}