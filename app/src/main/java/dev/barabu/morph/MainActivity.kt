package dev.barabu.morph

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.DrawableRes
import dev.barabu.morph.button.*
import dev.barabu.morph.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private var isRectButtonInTextMode = true
    private var isLinearProgressButtonInTextMode = true
    private var isCycleProgressButtonInTextMode = true
    private var isMultiStateButtonInTextMode = true
    private var isMtsButtonInTextMode = true
    private var isDottedButtonInTextMode = true
    private var isLineAndDottedButtonInTextMode = true

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        // Простой morph формы и цвета
        viewBinding.buttonText.apply {
            setOnClickListener {

                if (isRectButtonInTextMode) {
                    morphToSuccess(
                        color(android.R.color.holo_orange_dark),
                        dimen(R.dimen.cycle_outline_stroke_width_2dp).toInt()
                    )
                } else {
                    morphToRect(
                        integer(R.integer.animation_duration),
                        string(R.string.button_text_simple),
                        color(android.R.color.white),
                        color(android.R.color.darker_gray),
                        color(android.R.color.holo_blue_dark),
                        dimen(R.dimen.cycle_outline_stroke_width_2dp).toInt()
                    )
                }
                isRectButtonInTextMode = !isRectButtonInTextMode
            }
            morphToRect(
                integer(R.integer.animation_duration),
                string(R.string.button_text_simple),
                color(android.R.color.white),
                color(android.R.color.darker_gray),
                color(android.R.color.holo_blue_dark),
                dimen(R.dimen.cycle_outline_stroke_width_2dp).toInt()
            )
        }

        // Горизонтальная линия прогресса
        viewBinding.buttonLinearProgress.apply {
            setOnClickListener {
                if (isLinearProgressButtonInTextMode) {
                    startLinearProgress()
                } else {
                    morphToRect(
                        integer(R.integer.animation_duration),
                        string(R.string.button_text_line)
                    )
                }
                isLinearProgressButtonInTextMode = !isLinearProgressButtonInTextMode
            }
            morphToRect(0, string(R.string.button_text_line))
        }

        // Круговой прогресс из одноцветного сегмента
        viewBinding.buttonCircularColoredProgress.apply {
            setOnClickListener {

                if (isCycleProgressButtonInTextMode) {
                    startCircularProgress(
                        color(R.color.cycle_progress_foreground),
                        color(R.color.cycle_progress_background),
                        Color.TRANSPARENT,
                        0f
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
                        stopCircularProgress(
                            OpResult.Failure,
                            color(android.R.color.holo_green_light),
                            color(android.R.color.holo_green_dark),
                            color(android.R.color.holo_red_light),
                            color(android.R.color.holo_red_dark)
                        )
                    }, 2000)
                } else {
                    morphToRect(
                        integer(R.integer.animation_duration),
                        string(R.string.button_text_circle_color)
                    )
                }
                isCycleProgressButtonInTextMode = !isCycleProgressButtonInTextMode
            }
            morphToRect(0)
        }

        // Круговой прогресс из градиентной окружности по кромке
        viewBinding.buttonCircularGradientProgress.apply {
            setOnClickListener {
                if (isMultiStateButtonInTextMode) {
                    startCircularProgress(
                        color(R.color.cycle_progress_background),
                        color(R.color.cycle_progress_foreground),
                        Color.TRANSPARENT,
                        0f
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
                        stopCircularProgress(
                            OpResult.Success,
                            color(android.R.color.holo_green_light),
                            color(android.R.color.holo_green_dark),
                            color(android.R.color.holo_red_light),
                            color(android.R.color.holo_red_dark)
                        )
                    }, 2000)
                } else {
                    morphToRect(
                        integer(R.integer.animation_duration),
                        string(R.string.button_text_circle_gradient_edge)
                    )
                }
                isMultiStateButtonInTextMode = !isMultiStateButtonInTextMode
            }
            morphToRect(0, string(R.string.button_text_circle_gradient_edge))
        }

        // Круговой прогресс из градиентной окружности внутри View
        viewBinding.buttonMts.apply {

            setOnClickListener {
                if (isMtsButtonInTextMode) {
                    startCircularProgress(
                        color(R.color.ds_mts_pink),
                        Color.WHITE,
                        color(R.color.ds_mts_red),
                        dimen(R.dimen.cycle_progress_padding_2dp)
                    )

                    Handler(Looper.getMainLooper()).postDelayed({
                        stopCircularProgress(
                            OpResult.Success,
                            color(android.R.color.holo_blue_light),
                            color(android.R.color.holo_blue_dark),
                            color(android.R.color.holo_red_light),
                            color(android.R.color.holo_red_dark),
                            color(android.R.color.darker_gray),
                            dimen(R.dimen.cycle_outline_stroke_width_2dp).toInt()
                        )
                    }, 2000)

                } else {
                    morphToFabRect(
                        integer(R.integer.animation_duration),
                        string(R.string.button_text_circle_gradient_inside)
                    )
                }
                isMtsButtonInTextMode = !isMtsButtonInTextMode
            }
            morphToFabRect(0, string(R.string.button_text_circle_gradient_inside))
        }

        // Круговой прогресс из градиентной окружности точек внутри View
        viewBinding.buttonDotted.apply {
            setOnClickListener {
                if (isDottedButtonInTextMode) {
                    startCircularProgress(
                        primaryColor = Color.WHITE,
                        secondaryColor = color(R.color.ds_mts_red),
                        backgroundColor = color(R.color.ds_mts_red),
                        padding = dimen(R.dimen.cycle_progress_padding_4dp)
                    )

                    Handler(Looper.getMainLooper()).postDelayed({
                        stopCircularProgress(
                            OpResult.Success,
                            color(android.R.color.holo_blue_light),
                            color(android.R.color.holo_blue_dark),
                            color(android.R.color.holo_red_light),
                            color(android.R.color.holo_red_dark),
                         )
                    }, 2000)
                } else {
                    morphToFabRect(
                        integer(R.integer.animation_duration),
                        string(R.string.button_text_circle_dots_gradient)
                    )
                }
                isDottedButtonInTextMode = !isDottedButtonInTextMode
            }
            morphToFabRect(0, string(R.string.button_text_circle_dots_gradient))
        }

        viewBinding.buttonDottedOutlined.apply {
            setOnClickListener {
                //button_text_circle_dots_gradient_edge
                if (isLineAndDottedButtonInTextMode) {
                    startCircularProgress(
                        primaryColor = Color.WHITE,
                        secondaryColor = color(R.color.ds_mts_red),
                        backgroundColor = color(R.color.ds_mts_red),
                        padding = dimen(R.dimen.cycle_progress_padding_4dp)
                    )

                    Handler(Looper.getMainLooper()).postDelayed({
                        stopCircularProgress(
                            OpResult.Success,
                            color(android.R.color.white),
                            color(R.color.gray_A200),
                            color(android.R.color.holo_red_light),
                            color(android.R.color.holo_red_dark),
                            color(R.color.ds_mts_red),
                            dimen(R.dimen.cycle_outline_stroke_width_2dp).toInt(),
                            R.drawable.ic_done_red,
                            R.drawable.ic_error
                        )
                    }, 2000)
                } else {
                    morphToFabRect(
                        integer(R.integer.animation_duration),
                        string(R.string.button_text_circle_dots_gradient)
                    )
                }

                isLineAndDottedButtonInTextMode = !isLineAndDottedButtonInTextMode
            }
            morphToFabRect(0, string(R.string.button_text_circle_dots_gradient))
        }
    }

    private fun MorphingButton.morphToRect(
        duration: Int,
        text: String = getString(R.string.text_button),
        colorNormal: Int = color(android.R.color.holo_blue_light),
        colorPressed: Int = color(android.R.color.holo_blue_dark),
        strokeColor: Int = Color.TRANSPARENT,
        strokeWidth: Int = 0
    ) {
        val params = MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_2dp),
            width = dimen(R.dimen.button_rectangle_width).toInt(),
            height = dimen(R.dimen.button_rectangle_height).toInt(),
            colorNormal = colorNormal,
            colorPressed = colorPressed,
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            duration = duration,
            text = text
        )
        morph(params)
    }

    private fun MorphingButton.morphToFabRect(
        duration: Int,
        text: String = getString(R.string.text_button),
        strokeColor: Int = Color.TRANSPARENT,
        strokeWidth: Int = 0
    ) {
        val params = MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_rectangle_width).toInt(),
            height = dimen(R.dimen.button_rectangle_height).toInt(),
            colorNormal = color(R.color.ds_mts_red),
            colorPressed = color(android.R.color.holo_red_dark),
            duration = duration,
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            text = text,
            icon = AnchorIcon(0, 0, R.drawable.ic_send, 0)
        )
        morph(params)
    }

    private fun MorphingButton.morphToSuccess(
        strokeColor: Int = Color.TRANSPARENT,
        strokeWidth: Int = 0
    ) {
        val params = MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorNormal = color(android.R.color.holo_green_light),
            colorPressed = color(android.R.color.holo_green_dark),
            duration = integer(R.integer.animation_duration),
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            icon = AnchorIcon(l = R.drawable.ic_done)
        )
        morph(params)
    }

    private fun ProgressMorphingButton.startLinearProgress() {
        val color = color(R.color.cycle_progress_clipping)
        val progressForegroundColor = color(R.color.cycle_progress_foreground)
        val progressBackgroundColor = color(R.color.cycle_progress_background)
        val progressCornerRadius = dimen(R.dimen.corner_radius_4dp)
        val width = dimen(R.dimen.button_rectangle_width).toInt()
        val height = dimen(R.dimen.button_progress_height).toInt()
        val duration = integer(R.integer.animation_duration)

        morphToProgress(
            color,
            progressForegroundColor,
            progressBackgroundColor,
            progressCornerRadius,
            width,
            height,
            duration,
            0f,
            Color.TRANSPARENT,
            0
        )
    }

    /**
     * Если используется градиентный прогресс (gradient sweep), то цвет меняется по кругу CW
     * от градуса 0 (startColor) до градуса 360 (endColor). Канву мы также вращаем CW, то есть
     * получается, что цвет движется CW и endColor движется в "голове". "Голова" вращения должна
     * быть более выраженного цвета, чем "хвост". Здесь primaryColor - это цвет "головы", а
     * secondaryColor - цвет "хвоста".
     */
    private fun ProgressMorphingButton.startCircularProgress(
        primaryColor: Int,
        secondaryColor: Int,
        backgroundColor: Int,
        padding: Float
    ) {
        val progressCornerRadius = dimen(R.dimen.corner_radius_fab)
        val width = dimen(R.dimen.button_rectangle_height).toInt()
        val height = dimen(R.dimen.button_rectangle_height).toInt()
        val duration = integer(R.integer.animation_duration)

        // Это коррекция для градиентного прогресса
        val (headColor, tailColor) = if (this is Gradient) {
            secondaryColor to primaryColor
        } else {
            primaryColor to secondaryColor
        }

        morphToProgress(
            backgroundColor,
            headColor,
            tailColor,
            progressCornerRadius,
            width,
            height,
            duration,
            padding,
            Color.TRANSPARENT,
            0
        )
    }

    private fun ProgressMorphingButton.stopCircularProgress(
        result: OpResult,
        successColorNormal: Int,
        successColorPressed: Int,
        failureColorNormal: Int,
        failureColorPressed: Int,
        strokeColor: Int = Color.TRANSPARENT,
        strokeWidth: Int = 0,
        @DrawableRes successIcon: Int = R.drawable.ic_done,
        @DrawableRes failureIcon: Int = R.drawable.ic_error
    ) {
        when (result) {
            OpResult.Success -> {
                morphToResult(
                    successColorNormal,
                    successColorPressed,
                    dimen(R.dimen.corner_radius_fab),
                    dimen(R.dimen.button_square).toInt(),
                    dimen(R.dimen.button_square).toInt(),
                    300,
                    successIcon,
                    strokeColor,
                    strokeWidth
                )
            }
            OpResult.Failure -> {
                morphToResult(
                    failureColorNormal,
                    failureColorPressed,
                    dimen(R.dimen.corner_radius_fab),
                    dimen(R.dimen.button_square).toInt(),
                    dimen(R.dimen.button_square).toInt(),
                    300,
                    failureIcon,
                    strokeColor,
                    strokeWidth
                )
            }
        }
    }
}